# Domain-Driven Design (DDD) Learning Notes

## 1. Introduction to DDD
Structure and language of the code that matches business language
Purpose - Make your code speak the same language as the business.
Why - Avoids translation errors(less misunderstanding) between business and tech. DDD aims to have one ubiquitous language.
If developer understands the business they know exactly where to look for a particular feature(incase of issue), Instead of spending weeks decoding



## 2. Bounded Context
Segregating layers based on business functionality.

But lets say I have already deployed based on SubDomain. 
Domain: hiring, subDomain: Interview, Job search, Apply
Here in normal senario everything uses same Job detail entity. but here each service has it own Job entity
Why I need bounded context now.

But in practice, a subdomain can contain multiple bounded contexts if there are different perspectives/business functions. Below is the example

Lets say I have CartService
Eg:
invalid
    CartController 
    CartService 
    CartRepository
    CartValidator 

Why not valid: Different business functionality(Customer perspective, Marketing team's perspective, etc) make use of same code.
How CartService have addToCart(for Customer), identifyAbandonedCarts(for Marketing)

valid
    ShoppingCartService
    CartMarketingService
    CartAnalyticsService

Now Model can have same name but will have different properties in it. It is bounded context(seggregated on business functionality)

Learnt: Bounded context is Segragating layers based on business functionality not on technical functionality.

### Types of Bounded Context
Core - you core business subdomain - eg: Cart Management
Supporting subdomains - supports the cart - eg: Inventory (cart talks to it to know the available stock)
Generic Subdomains - common ones - Eg: Authentication, Logging




## 3. Context Map
In real world senario, you might have multiple bounded context. How they are going to communicate if needed.
The message going to flow between bounded context is domain event. Eg: TicketBooked not getEventInfo... getEventInfo os part of TicketBooked

### Relationship Types
What all scenario the bounded context is communicating

**Upstream/Downstream** - Downstream system needs upstream data (depends on data)
eg: Order Processing → Shipping: Shipping can't start until order is processed

**Customer/Supplier** - One context requests services from another (serves the data)
E-commerce Cart → Inventory Service: Cart checks product availability

**Partnership** - Two context migth have scenario to work together and both depends on eachother
eg: Orders ↔ Inventory: Orders reduce inventory, low inventory affects order acceptance

**Shared kernal** - Two contexts share some common code/library
eg: Canonical data model

### Context Map Examples
A context map is basically your communication strategy blueprint for all bounded contexts based on above senario.

Upstream/Downstream - Job posting context -> Apply context(once job posting is created Apply need to know that)
Communication choice: Async Messaging

Customer/Supplier - Apply context -> Interview context(apply need info from interview detail)
Communication choice: sync API call

Partnership - Search context and Sort context
Search needs Sorting to order the results meaningfully.
Sorting needs Search to provide the product set it will sort.
Communication choice: Event bus

Shared kernal - Internal apply context and External apply context uses same CDM
Communication choice: Nothing needed. But spent correct time on CDM design

### Event Storming 
Event Storming is the way to find the mapping between subdomains. big brainstorming session for discovering and mapping your domain considering domain events



## 4. Anti-Corruption Layer (ACL)
But this will not violate below by having dependency?
This violates bounded context isolation — you've turned two independent contexts into a tangled system.
eg: service A dependent on service B's model

There comes ACL (Anti-Corruption Layer)
ACL should be the exposed layer from your bounded context. It should receive or send data to different bounded context.
ACL translates the data which is getting received which is  acceptable for that bouned context. Like a adaptor
we need ACL per bounded context



## 5. Entities vs Value Objects
**Entity** -> have its own life cycle
**ValueObject** -> No life cycle only static

eg:
Customer -> Entity
{
    Address -> valueObject(static)
    Load -> Entity( have life cycle - loan open, closed etc)
}



## 6. Aggregate Root
An Aggregate Root is the single entry point for making changes to the entities, within a bounded context.
ACL will be on top of this to translate the data

Why?
It handles all the validation and then it passes to entity to change



## 7. Hexogonal Architecture (3-layer structure)

It has 3 layers
Application layer
Domain layer
Infrastucture layer

### Domain layer
It contains your core business function
Aggregate root
    Aggregate

Eg:
Cart
    private UUID id;
    private List<CartItem> items;
    private BigDecimal price;

    public void addToCart(Product product) {
        validateProduct(product);
        items.add(new CartItem(product));
        price = price.add(product.getPrice());
    }

Cart service
    private final CartRepository CartRepository;

    public DomainCartService(CartRepository CartRepository) {
        this.CartRepository = CartRepository;
    }

    @Override
    public void addProduct(UUID id, Product product) {
        Cart cart = getCart(id);
        cart.addCart(product);
        cartRepository.save(Cart);
    }

Repository
public interface CartRepository{}

Things to know
These all 3 class will be in same package. 
Incase if you need to configure something as been. We won’t register it because, from a domain perspective, this is in the inside part, and Spring configuration is on the outside.

### Infrastructure layer
contains the logic needed to run the application.
eg: configuration classes, JPA Repository file, JPA Entity files, Adaptor class(User Domain to Jpa Entity Object)

### Application layer
Through the application layer, the user or any other program interacts
Eg: RESTful controllers, Adaptor class(User object to Domain Object)

Flow:
Scenario: User add to cart
1) Cart data from user through Rest API(Application layer)
2) Transform User sent data to domain Data using adaptor in Application Service(Application layer)
3) Cart data from Application Service(Application layer) to Domain layer
4) Aggregate root validates the Cart data(Domain layer)
5) Aggregate root to Returns the data back (on either post or get) to Aplication layer
6) Aplication layer to Infrastructure layer
7) Transform Cart domain object to JPA entity(Infrastructure layer)
8) JpaRepository saves the data(Infrastructure layer)


Sample folder structure

com/example
│
├── cart                          # Cart Bounded Context
│   ├── application               # Application Layer
│   │   ├── CartController.java
│   │   ├── CartApplicationService.java
│   │   ├── CartRequestDTO.java
│   │   └── acl                   # Anti-Corruption Layer
│   │       └── PricingACL.java   # Translates Pricing context data
│   │
│   ├── domain                    # Domain Layer
│   │   ├── Cart.java             # Aggregate Root
│   │   ├── CartItem.java         # Entity
│   │   ├── Money.java            # Value Object
│   │   ├── CartRepository.java   # Repository Interface
│   │   └── CartDomainService.java
│   │
│   └── infrastructure            # Infrastructure Layer
│       ├── CartRepositoryImpl.java
│       ├── CartEntity.java
│       └── CartEntityMapper.java
│
├── pricing                       # Pricing Bounded Context
│   ├── application
│   │   ├── PricingController.java
│   │   ├── PricingApplicationService.java
│   │   ├── PricingRequestDTO.java
│   │   └── acl                   # Anti-Corruption Layer
│   │       └── CartACL.java      # Translates Cart context data
│   │
│   ├── domain
│   │   ├── PricingRule.java      # Aggregate Root
│   │   ├── Discount.java         # Entity
│   │   ├── Money.java            # Value Object
│   │   ├── PricingRepository.java
│   │   └── PricingDomainService.java
│   │
│   └── infrastructure
│       ├── PricingRepositoryImpl.java
│       ├── PricingEntity.java
│       └── PricingEntityMapper.java
│
├── shared                        # Shared Kernel (minimal)
│   ├── Money.java                # Common Value Object
│   ├── ProductId.java            # Common Value Object
│   └── DomainEvent.java          # Base Event Class
│
└── config                        # Application Configuration
    ├── ApplicationConfig.java
    └── DatabaseConfig.java

Eg: Cart needs Pricing...communication between 2 Bounded context 

Application layer
public class CartApplicationService {
    private final PricingACL pricingACL;
    public CartResponse calculateCartTotal(CartRequest request) {
        PricingResponse pricing = pricingACL.getPricing(request.getItems());
        Cart cart = buildCart(request, pricing);
        return mapToResponse(cart);
    }
}

public class PricingACL {
    private final PricingServiceClient pricingClient; // HTTP client or message bus
    
    public CartDiscount getPricing(List<CartItem> items) {
        PricingRequest pricingRequest = translateToPricingRequest(items);
        PricingResponse response = pricingClient.calculatePrice(pricingRequest);
        return translateToCartDiscount(response);
    }
}

//Infrastructure layer
@Component
public class PricingServiceClient {
    @Autowired
    private RestTemplate restTemplate;
    
    public PricingResponse calculatePrice(PricingRequest request) {
        return restTemplate.postForObject("/pricing/calculate", request, PricingResponse.class);
    }
}



Main thing to notice
1) There will not be any method to method call between class in a domain layer of bounded context
2) All call happens only in apllication layer
    CartApplicationService(Application layer) -> Cart(Root)(Domain layer)
    CartApplicationService(Application layer) -> CartRepository(Domain layer)
    not Cart(Root)(Domain layer) to CartRepository(Domain layer)
3) There should be any spring annotations inside domain layer
4) Even http configuration for particular endpoint should be in Infrastucture layer
5) Calling the another bounded context should be in ACL layer
6) Iterface not needed for Application service