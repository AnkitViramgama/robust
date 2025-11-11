# Deployment Guide

This guide covers deploying the modular application to various environments.

## Docker Deployment

### Development

```bash
docker-compose up -d
```

### Production

1. **Set environment variables** in `docker-compose.yml`:

```yaml
services:
  backend:
    environment:
      JWT_SECRET: ${JWT_SECRET}
      MONGO_USERNAME: ${MONGO_USERNAME}
      MONGO_PASSWORD: ${MONGO_PASSWORD}
```

2. **Create `.env` file**:

```env
JWT_SECRET=your-production-secret-key
MONGO_USERNAME=admin
MONGO_PASSWORD=secure-password
```

3. **Start services**:

```bash
docker-compose --env-file .env up -d
```

## Kubernetes Deployment

### 1. Create Namespace

```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: modular-app
```

### 2. MongoDB Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: mongodb
  namespace: modular-app
spec:
  replicas: 1
  selector:
    matchLabels:
      app: mongodb
  template:
    metadata:
      labels:
        app: mongodb
    spec:
      containers:
      - name: mongodb
        image: mongo:7.0
        ports:
        - containerPort: 27017
        env:
        - name: MONGO_INITDB_ROOT_USERNAME
          valueFrom:
            secretKeyRef:
              name: mongo-secret
              key: username
        - name: MONGO_INITDB_ROOT_PASSWORD
          valueFrom:
            secretKeyRef:
              name: mongo-secret
              key: password
        volumeMounts:
        - name: mongo-storage
          mountPath: /data/db
      volumes:
      - name: mongo-storage
        persistentVolumeClaim:
          claimName: mongo-pvc
```

### 3. Backend Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: backend
  namespace: modular-app
spec:
  replicas: 3
  selector:
    matchLabels:
      app: backend
  template:
    metadata:
      labels:
        app: backend
    spec:
      containers:
      - name: backend
        image: your-registry/modular-backend:latest
        ports:
        - containerPort: 8080
        env:
        - name: MONGO_HOST
          value: "mongodb-service"
        - name: JWT_SECRET
          valueFrom:
            secretKeyRef:
              name: app-secret
              key: jwt-secret
```

### 4. Frontend Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: frontend
  namespace: modular-app
spec:
  replicas: 2
  selector:
    matchLabels:
      app: frontend
  template:
    metadata:
      labels:
        app: frontend
    spec:
      containers:
      - name: frontend
        image: your-registry/modular-frontend:latest
        ports:
        - containerPort: 80
```

### 5. Services & Ingress

```yaml
---
apiVersion: v1
kind: Service
metadata:
  name: backend-service
  namespace: modular-app
spec:
  selector:
    app: backend
  ports:
  - port: 8080
    targetPort: 8080

---
apiVersion: v1
kind: Service
metadata:
  name: frontend-service
  namespace: modular-app
spec:
  selector:
    app: frontend
  ports:
  - port: 80
    targetPort: 80

---
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: app-ingress
  namespace: modular-app
spec:
  rules:
  - host: app.example.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: frontend-service
            port:
              number: 80
      - path: /api
        pathType: Prefix
        backend:
          service:
            name: backend-service
            port:
              number: 8080
```

## Cloud Platform Deployment

### AWS ECS

1. **Build and push images** to ECR
2. **Create task definitions**
3. **Set up ECS service** with load balancer
4. **Configure MongoDB** with DocumentDB or MongoDB Atlas

### Google Cloud Run

1. **Build containers**:

```bash
gcloud builds submit --tag gcr.io/PROJECT_ID/modular-backend backend/
gcloud builds submit --tag gcr.io/PROJECT_ID/modular-frontend frontend/
```

2. **Deploy services**:

```bash
gcloud run deploy backend \
  --image gcr.io/PROJECT_ID/modular-backend \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated

gcloud run deploy frontend \
  --image gcr.io/PROJECT_ID/modular-frontend \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated
```

### Azure Container Apps

```bash
az containerapp create \
  --name modular-backend \
  --resource-group my-rg \
  --image your-registry/modular-backend:latest \
  --environment my-env
```

## Database Migration

### Production MongoDB Setup

1. **Enable authentication**
2. **Create backup user**
3. **Set up replica set** for high availability
4. **Enable TLS/SSL**

### Connection String

```yaml
MONGO_URI: "mongodb://user:pass@host1:27017,host2:27017,host3:27017/modular_app?replicaSet=rs0&ssl=true"
```

## SSL/TLS Configuration

### Using Let's Encrypt with Nginx

```nginx
server {
    listen 443 ssl http2;
    server_name app.example.com;

    ssl_certificate /etc/letsencrypt/live/app.example.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/app.example.com/privkey.pem;

    location / {
        proxy_pass http://frontend:80;
    }

    location /api {
        proxy_pass http://backend:8080;
    }
}
```

## Monitoring & Logging

### Application Logs

Backend logs are in JSON format for easy parsing by log aggregators.

### Prometheus Metrics

Add to `backend/main-app/pom.xml`:

```xml
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

Metrics available at: `/actuator/prometheus`

### Health Checks

- Backend: `/actuator/health`
- Frontend: `/` (returns 200 if serving)

## Scaling

### Horizontal Scaling

**Backend**: Stateless, can scale to multiple replicas
**Frontend**: Static files, can use CDN
**MongoDB**: Use replica set or managed service

### Load Balancing

Use cloud load balancers or Kubernetes Ingress for distributing traffic.

## Backup & Disaster Recovery

### MongoDB Backups

```bash
# Backup
mongodump --uri="mongodb://user:pass@host:27017/modular_app" --out=/backups/$(date +%Y%m%d)

# Restore
mongorestore --uri="mongodb://user:pass@host:27017/modular_app" /backups/20240101
```

### Automated Backups

Set up cron job or use managed MongoDB service backup features.

## Security Checklist

- [ ] Use strong JWT secret
- [ ] Enable MongoDB authentication
- [ ] Use TLS/SSL for all connections
- [ ] Set up firewall rules
- [ ] Regular security updates
- [ ] Rate limiting on API endpoints
- [ ] Input validation
- [ ] CORS configuration
- [ ] Security headers
- [ ] Regular backups
