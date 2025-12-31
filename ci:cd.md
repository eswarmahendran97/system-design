# Ci/Cd

## CI/CD process
Env:
The AKS cluster need to be provisioned with required namespaces.

Source & Config Setup:
GitHub repository with application code and Kubernetes manifests (Deployment, Service, Ingress). 

CI Pipeline (Continuous Integration):
A webhook was configured from GitHub to Jenkins - build triggers in code push, pr

The Jenkins pipeline included the following stages:
namespace allocation
Code Checkout & Build
Unit Testing
Code Quality Check
integration testing
Docker Image Build & Push – Create a Docker image and push it to Azure Container Registry (ACR).

CD Pipeline (Continuous Deployment):
Next step: Jenkins pipeline updated the Kubernetes Deployment manifest with the new image tag and applied it on the AKS cluster using kubectl.


## NameSpace in kubernetes
A namespace is a way to divide cluster resources between multiple users or teams. There is default is also available
Eg: for my team I create a namespace and deployment my service there


## Importatnt files in kubernetes
deployment.yaml: Defines how to deploy and manage replica sets of your application pods. pods, replicas, scaling
service.yaml: Exposes pods internally or externally to enable network access.
A Service gives them a stable IP and DNS name to pods. Exposes service on a fixed port. Provisions a cloud load balancer 
ingress.yaml: Manages external HTTP/HTTPS routing.
sits in front of Services and routes HTTP(S) requests to them based on rules.
configmap.yaml: Stores non-sensitive configuration data for pods. configuration data
secret.yaml: Stores sensitive information like passwords and tokens securely. secret data

## Increase number of pods
Increase the replicas count in your deployment.yaml:
    replicas: 5

## Deploy without downtime
Kube by default supports it

deployment.yaml
strategy:
  type: RollingUpdate
  rollingUpdate:
    maxUnavailable: 1
    maxSurge: 1

## Important things configured in deployment.yaml
replicas
resources
strategy
livenessProbe - Restarts the container if the app is running but unhealthy  
readinessProbe - Controls whether the pod should receive traffic.

## Types of Services
ClusterIP
NodePort
LoadBalancer

## How kube creates its own LB, 
service.yaml
    type: ClusterIP

## How docker Image is deployed in container
deployment.yaml contains:

spec:
    template:
        spec:
            containers:
              - name: questions-service
                image: current-image

on cd step Jenkins will update this image in deployment.yaml. using kubectl command applies it


## How 8080 is exposed as 443
1) Application starts in 8080 in your pod
2) in container it can be accessble as http://ip-address:8080

user will make a call by service-url/endpoint

currently user cnt access anything since ip is not exposed to outside world
so we can configure service with pod

service.yaml
"ports": [ 
            { 
                "name": "http", 
                "protocol": "TCP", 
                "port": 8080, 
                "targetPort": 8080 
            } 
        ]
now when user hits http://service-url:8080 it forwards the traffic to http://ip-address:8080 in the cluster

but user cant hit by port number(8080) and http is not secured

Ingress helps here
Ingress resource defines the rules, but the Ingress Controller is what actually terminates HTTPS (443), decrypts the request using tls-secret, and forwards HTTP (8080) to the service/pod

spec:
  tls:
    - hosts:
        - myapp.example.com
      secretName: tls-secret   # stores your certificate
  rules:
    - host: myapp.example.com
      http:
        paths:
          - path: /
            pathType: Prefix
            backend:
              service:
                name: myapp-service
                port:
                  number: 8080

flow:
HTTPS (443) → lb -> Ingress Controller -> service -> pod


## how to use secret manager
secrets can be managed in secrets.yaml. To make it more secured we use secret manager

steps:
create new kind: SecretProviderClass (which is like service, deployment, ingress)
configure csi driver
mount it in deployment.yaml


## Why Helms:
Helm is the package manager for Kubernetes

Now we need to manually do/configure
Certificate in ingress
new Image version in deployment
SecretProviderClass for secret

What if someone bake it give directly... That is helm

Helm uses your chart + values.yaml to render Kubernetes manifests (Deployment.yaml, Service.yaml, Ingress.yaml, etc.).


## flow using helms:
GitHub Push
    ↓
Jenkins Pipeline
    ↓
Docker Build & Push → ACR
    ↓
Helm Install/Upgrade (chart + values.yaml)
    ↓
Kubernetes API → Deployment / Service / Ingress updated
    ↓
Pods run new image, endpoints exposed
