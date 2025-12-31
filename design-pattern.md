# Design pattern

## Creational

### Singleton
When you need only one instance globally

class Singleton{
    static Object e;
    private Singleton(){}

    public static synchronized Object getObject(){
        if(e == null)
            e = new Object();
        return e;
    }
}

//usage
Singleton.getObject();

Singleton Enum

enum Singleton{

   INSTANCE;
   private Object connection;

   Singleton(){
       //construct connection
       System.out.println("hello");
   }

   public Object getConnection(){
       return connection;
   }
}


class TestSingleton{
    public static void main(String[] args) {
        Singleton singleton = Singleton.INSTANCE;
        System.out.println(singleton.getConnection());
    }
}


### Factory
The responsibility of object creation to a dedicated central factor, promoting loose coupling between the creator and the client

interface ButtonType {
    void buttonType();
}

class Submit implements ButtonType {
    public void buttonType() {
        System.out.println("Submit button");
    }
}

class Reset implements ButtonType {
    public void buttonType() {
        System.out.println("Reset button");
    }
}

class TypeFactory{
    ButtonType createButton(String type){
        return switch(type){
            case "submit" -> new Submit();
            case "reset" -> new Reset();
            default -> new Submit();
        }
    }
}

public class Main{
    public static void main(String[] args) {
        TypeFactory factory = new TypeFactory();
        ButtonType b1 = factory.createButton("submit");
        b1.buttonType(); // Output: Submit button
    }
}

you can also do the same thing without factory
like
public class Main{
    public static void main(String[] args) {
        ButtonType b1 = new Submit();
        b1.buttonType(); // Output: Submit button
    }
}
but here it becomes tightly coupled. You maintaining the lifecycle of another object. 

Learnt:
Tomorrow if new shape come we will change the code in both ways but when you use Factory we will not change client code we will change only Factory.
But any way u need to change in client to add ButtonType b1 = factory.createButton("submit"); right?
Not necessary. type can be externalized (from DB, UI etc)


### Abstract factory

Its is used to create families of related objects (not just one). Its like factory of factories. 
This pattern is commonly used when we start using the Factory Method Pattern, and we need to evolve our system to a more complex system. It centralizes the product creation code in one place

Now suppose we also want Colors (Red, Black) in addition to Shapes.

interface ButtonColor{
    void buttonColor();
}

class Primary implements ButtonColor {
    public void buttonColor() {
        System.out.println("Blue button");
    }
}

class Secondary implements ButtonColor {
    public void buttonColor() {
        System.out.println("black button");
    }
}

interface AbstractButtonFactory{
    void create(string factory);
}

(family 1)
class ColorFactory implements AbstractFactory{
    ButtonColor create(String type){
        return switch(type){
            case "red" -> new Primary();
            case "balck" -> new Secondary();
            default -> new Primary();
        }
    }
}

(family 2)
class TypeFactory implements AbstractFactory{
    ButtonType create(String type){
        return switch(type){
            case "submit" -> new Submit();
            case "reset" -> new Reset();
            default -> new Submit();
        }
    }
}

class FactoryProvider {
    public static AbstractFactory<?> getFactory(String choice) {
        return switch (choice.toLowerCase()) {
            case "color" -> new ColorFactory();
            case "type" -> new TypeFactory();
            default -> throw new IllegalArgumentException("Unknown factory: " + choice);
        };
    }
}

public class Main {
    public static void main(String[] args) {
        String factory = "color"; // from UI
        String color = "primary"; // from UI
        AbstractFactory colorFactory = FactoryProvider.getFactory(factory);
        ButtonColor color = typeFactory.create(color);
        color.buttonColor(); // Output: Blue button


        String factory = "type"; // from UI
        String type = "submit"; // from UI
        AbstractFactory typeFactory = FactoryProvider.getFactory(factory);
        ButtonType type = typeFactory.create(type);
        type.buttonType(); // Output: Submit button
    }
}


### Builder

Use To construct a complex object step by step and Immutablity.

Immutablity
All fields are final.
No setters are exposed.



## Structural
It’s a design pattern that defines how classes and objects are combined to build larger
Focus is on composition — to make systems easier to extend.
How to arrange things together nicely.

### Adaptor
used to make two unrelated interfaces can work together.
Use there are multiple 3rd party library
Make request type and response type as same using CDM or common types

interface DocumentReader{
    void read();
}

class AdobeFileReaderLib{
    void scanFile(){
        ...code
    }
}

class CanvaFileReaderLib{
    void readFile(){
        ...code
    }
}

class AdobeAdaptor implements DocumentReader{
    AdobeFileReaderLib lib = new AdobeFileReaderLib();
    void read(){
        lib.scanFile();
    }
}

class CanvaAdaptor implements DocumentReader{
    CanvaFileReaderLib lib = new CanvaFileReaderLib();
    void read(){
        lib.readFile();
    }
}

// === Client code ===
public class Main {
    public static void main(String[] args) {
        DocumentReader adobeReader = new AdobeAdaptor();
        DocumentReader canvaReader = new CanvaAdaptor();

        adobeReader.read("document.pdf");
        canvaReader.read("poster.canva");
    }
}


### Decorator
It lets you “decorate” (add extra features) to an object without changing its original class.

public interface FileReader {
    void readFile();
}

public class FileReaderImpl implements FileReader {
    public void readFile() {
        System.out.println("Reading...");
    }
}

public abstract class AbstractDecorator implements FileReader{
    FileReader reader;
    AbstractDecorator(FileReader reader) {
        this.reader = reader;
    }

    @Override
    public void readFile() {
        reader.readFile();
    }
}

public class Logging extends AbstractDecorator{
    Logging(FileReader reader) {
        super(reader);
    }

    public void readFile(){
        super.readFile();
        System.out.println("Logging..");
    }
}

public class Main {
    public static void main(String[] args) {
        FileReader reader = new Logging(new FileReaderImpl());
        reader.readFile();
    }
}

o/p:
Reading...
Logging..

Why we need AbstractDecorator here.. we can directly use Logging to implement FileReader?
No, now there is only one abstract method in FileReader but new things can be added. You cannot implement it in your decorator(Logging). 
So we will pass instance in constructor 
Logging(FileReader reader) {
    super(reader); // passing instance to next level, here FileReaderImpl will be passed
}

decorate it and call the next decorator
public void readFile(){
    System.out.println("Logging.."); //decorate
    super.readFile(); // calling action method
}

passing will stop in abstract decorator. There we will call original method to perform opeartion
FileReader reader;
AbstractDecorator(FileReader reader) {
    this.reader = reader;
}

@Override
public void readFile() {
    reader.readFile(); // calling original method to perform opearion
}


### Facade

simplifies interactions with complex subsystems.
Eg:
VegHotel
NonVegHotel
VegNonVegHotel

Clients needs menu. Instead of client having this complexity of getting menu
Client asks Facade. Facade handles the complexity

Facade{
    getVegMenu();
    getNonVegMenu();
    getVegNonVegMenu();
}

### proxy
Proxy provides a placeholder or surrogate for another object.
The Proxy design pattern provides a substitute or placeholder for another object to control access to it

When to use:
You want to defer the creation of an expensive object until it’s actually needed, e.g., a large image or a database connection.

Example: LargeImageProxy loads the real LargeImage only when display() is called.

// Subject
interface Image {
    void display();
}

// Real Subject
class RealImage implements Image {
    private String filename;

    public RealImage(String filename) {
        this.filename = filename;
        loadFromDisk();
    }

    private void loadFromDisk() {
        System.out.println("Loading " + filename + " from disk...");
    }

    @Override
    public void display() {
        System.out.println("Displaying " + filename);
    }
}

// Proxy
class ImageProxy implements Image {
    private RealImage realImage;
    private String filename;

    public ImageProxy(String filename) {
        this.filename = filename;
    }

    @Override
    public void display() {
        if (realImage == null) {
            realImage = new RealImage(filename); // load only when needed
        }
        realImage.display();
    }
}

// Client
public class ProxyDemo {
    public static void main(String[] args) {
        Image image = new ImageProxy("photo.jpg");

        // Image is not loaded yet
        System.out.println("Image object created.");

        // Image loaded only when display() is called
        image.display(); // first call: loads and displays
        image.display(); // second call: only displays, no loading
    }
}

### Composite
Composite is a structural design pattern that lets you compose objects into tree structures and then work with these structures as if they were individual objects.

Using the Composite pattern makes sense only when the core model of your app can be represented as a tree.
Eg: folders

## Behavioural

### Strategy
allows switching algorithms dynamically

Diff between strategy and factory 
factory -> gives u object from factory
strategy -> u send an object to context

interface PaymentStrategy{
    void pay();
}

class CardPayment implments PaymentStrategy{
    void pay{
         System.out.println("Card payment..");
    }
}

class UpiPayment implments PaymentStrategy{
    void pay{
         System.out.println("UPI payment..");
    }
}

class PaymentContext{
    PaymentStrategy payment;
    PaymentContext(PaymentStrategy payment){
        this.payment = payment;
    }

    void makePayment(){
        payment.pay()
    }
}

//Main
public class Main {
    public static void main(String[] args) {
        PaymentContext context = new PaymentContext(new UpiPayment());
        context.makePayment();
    }
}


### Observer
One object changes, and many others need to know about it.
Also Decoupling → Subject doesn’t care what observers do with the event.

class Subject{
    List<Observer> observers = new ArrayList<>();

    void subscribe(Observer observer){
        observers.add(observer);
    }

    void addData(Data data){
        // db save
        Event event = transform(data);
        bradcast(event)
    }

    void bradcast(Event event){
        for(var o: observers)
            o.notification(event);
    }

}

interface Observer{
    void notification(Event event);
}

class Observer1 implements Observer{
    void notification(Event event){
        //perform operation
    }
}


### outbox pattern

When our service writes both the domain data (e.g., orders table) and an "outbox" record (e.g., outbox_events table) in the same database transaction.

Example: Event table
CREATE TABLE outbox_events (
    event_id       UUID PRIMARY KEY,
    aggregate_type VARCHAR(50) NOT NULL,    -- e.g., "Order"
    aggregate_id   UUID NOT NULL,           -- e.g., order_id
    event_type     VARCHAR(50) NOT NULL,    -- e.g., "OrderCreated", "OrderPaid"
    payload        JSONB NOT NULL,          -- event body (decoupled from DB schema, only the data which you need to send)
    occurred_at    TIMESTAMP NOT NULL,
    processed      BOOLEAN DEFAULT FALSE    -- optional, if you need local retries
);

#### How it works:
Order data will be inserted in orders table.
Order event data is framed and inserted in outbox_events table.
Since both run in the same transaction, if one fails both will be rolled back.

#### Dual Write Issue
When your application needs to write to two systems (DB + Kafka), since most systems don’t support distributed transactions across DB + Kafka, there’s a risk that the two writes (risk that either one can fail).

Usually we can use Kafka Connect/CDC to avoid the dual write issue and guarantee data consistency between the two systems.

#### Extra We Get with Outbox
Kafka Connect will not need to know the whole order schema.
Easy to evolve schema without breaking consumers.
To know what change happened, received data needs to be interpreted. In Outbox, we send the event type.


### Command pattern
It encapsulates a request as an object(including method to call, the method’s arguments) to decoupling the sender from the receiver.
Instead of calling a method directly, wrap the method call + its arguments + its receiver into an object. Then you can treat that object uniformly: execute it, queue it, log it, undo it.

Eg: Runnable

Runnable as Command
interface Runnable{
    void run();
}

Any class that implements Runnable is a ConcreteCommand
public class MyRunnable implements Runnable {
    @Override
    public void run() {
       // some operation
    }
}

The Invoker (e.g., ExecutorService or Thread) doesn’t know the details of the receiver or the operation.
ExecutorService executor = Executors.newFixedThreadPool(2);
executor.submit(new MyRunnable())


Invoker only knows it can call run() on a Runnable (Command).
Because of this, the invoker is loosely coupled to the receiver — it only interacts with the Command object.

Question:
We will have mutiple commands... MyRunnable, YourRunnable that is also equal to subclass right
Reason is executor is now losely coupled with MyRunnable

Eg: text editor

interface TextEditorCommand {
    void execute(String letter);
    void undo();
}

class TextContent {
    String text = "";

    void write(String letter){
        text += letter;
    }

    void delete(){
        if(text.length() > 0)
            text = text.substring(0, text.length() - 1);
    }
}

class WriteCommand implements TextEditorCommand {
    private TextContent content;
    private String lastWritten = "";

    public WriteCommand(TextContent content){
        this.content = content;
    }

    @Override
    public void execute(String letter){
        content.write(letter);
        lastWritten = letter; // store for undo
    }

    @Override
    public void undo(){
        content.delete(); // remove last written character
    }
}

class DeleteCommand implements TextEditorCommand {
    private TextContent content;
    private String deletedChar = "";

    public DeleteCommand(TextContent content){
        this.content = content;
    }

    @Override
    public void execute(String letter){
        if(content.text.length() > 0){
            deletedChar = content.text.substring(content.text.length() - 1);
            content.delete();
        }
    }

    @Override
    public void undo(){
        content.write(deletedChar); // restore last deleted character
    }
}


class Main {
    public static void main(String[] args){
        TextContent tc = new TextContent();

        Stack<TextEditorCommand> history = new Stack<>();

        TextEditorCommand write = new WriteCommand(tc);
        TextEditorCommand delete = new DeleteCommand(tc);

        write.execute("a"); history.push(write);
        write.execute("b"); history.push(write);
        delete.execute(""); history.push(delete);

        System.out.println("Text before undo: " + tc.text); // prints "a"

        // Undo last command
        history.pop().undo();
        System.out.println("Text after undo: " + tc.text); // prints "ab"
    }
}


### Template 
Template method defines ta fixed workflow and it can provide default implementation that might be common for all or some of the subclasses

public abstract class HouseTemplate {
    public final void buildHouse(){
		buildFoundation();
		buildPillars();
		buildWalls();
		buildWindows();
		System.out.println("House is built.");
	}

    //default implementation
    private void buildFoundation() {
		System.out.println("Building foundation with cement,iron rods and sand");
	}

    private void buildWindows() {
		System.out.println("Building Glass Windows");
	}

	//methods to be implemented by subclasses
	public abstract void buildWalls();
	public abstract void buildPillars();
}

public class WoodenHouse extends HouseTemplate {

	@Override
	public void buildWalls() {
		System.out.println("Building Wooden Walls");
	}

	@Override
	public void buildPillars() {
		System.out.println("Building Pillars with Wood coating");
	}
}

public class HousingClient {
	public static void main(String[] args) {
		HouseTemplate houseType = new WoodenHouse();
		houseType.buildHouse();
	}
}

now every thing will be executed in order based on method defined in parent class

How RestTemplate use template pattern
RestTemplate.exchange(...) internally calls RestTemplate.execute()
RestTemplate.execute() defines a fixed workflow for making HTTP requests (create → send → handle → extract)


### Chain of reponsiblities
The request flows through until someone handles it

Filter in Java is class example

interface Filter{
    void doFilter(Request request, Response response, FilterChain chain);
}

class AuthFilter implements Filter {
    public void doFilter(Request request, Response response, FilterChain chain) {
        System.out.println("AuthFilter: checking authentication...");
        // Pass to next filter in the chain
        chain.doFilter(request, response);
    }
}

class LoggingFilter implements Filter {
    public void doFilter(Request request, Response response, FilterChain chain) {
        System.out.println("LoggingFilter: logging request...");
        chain.doFilter(request, response);
    }
}

class FilterChain{
    List<Filter> filters = New ArrayList<>();
    int currentPosition = 0;

    public void addFilter(Filter filter){
        filters.add(filter);
    }

    public void doFilter(Request request, Response response){
        if(filters.size() < currentPosition){
            Filter filter = filters.get(currentPosition++);
            filter.doFilter(request, response, chain);
        } else {
            response.data = "Processed request: " + request.data;
        }
    }
}

This is what Decorator pattern do?
No, here we are passing to chain of potential handlers. The handler can again pass it to next hanlder or can stop there itself.
This you can achieve using Decorator by making some tweaks but that is not decorator


### Mediator

Kepping a mediator to help one object interact with another. Making them loosley coupled


### Visitor
lets you perform operations/traversal on these elements without modifying their classes
Basically you have class structure... When you need perform a operation on one of the property of the class.. with out altering the class.. we can use Visitor

Eg:
class Electronics{
    String name;
    double price;

    //getter
    //setter
}

Now you need to alter a property : price.. to add tax and discount
Now you will add 2 new property, priceWithTax and priceWithDiscount

instead of altering the class we can use Visitor

interface ShoppingVisitor {
    void visit(Electronics electronics);
}

interface Product {
    void accept(ShoppingVisitor visitor);
}

class Electronics implements Product {
    String name;
    double price;

    //getter
    //setter

    public void accept(ShoppingVisitor visitor) {
        visitor.visit(this);
    }
}

class TaxCalculatorVisitor implements ShoppingVisitor {
    @Override
    public void visit(Electronics electronics) {
        System.out.println("Electronics tax: " + electronics.price * 0.15);
    }
}

class DiscountCalculatorVisitor implements ShoppingVisitor {
    @Override
    public void visit(Electronics electronics) {
        System.out.println("Electronics discount: " + electronics.price * 0.05);
    }
}

class Main{
    public static void main(String[] args) {
        Electronics ele = new Electronics("Laptop", 1000);

        ShoppingVisitor taxVisitor = new TaxCalculatorVisitor();
        ShoppingVisitor discountVisitor = new DiscountCalculatorVisitor();

        ele.accept(taxVisitor);
        ele.accept(discountVisitor);
    }
}

### Memento
It helps save and restore an object's state without exposing its internal details. It is like a "snapshot" that allows you to roll back changes if something goes wrong. 

When to use:
Undo functionality: When your application needs to include an undo function that lets users restore the state of an object after making modifications.
Snapshotting: When you need to enable features like versioning or checkpoints by saving an object's state at different times.
Transaction rollback: When there are failures or exceptions, like in database transactions, and you need to reverse changes made to an object's state.


### state
allows an object to change its behavior when its internal state changes.

Action of object will based on current state(Draft, publised, etc)