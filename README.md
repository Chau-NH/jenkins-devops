# jenkins-devpos
Apply a Shared Library to Jenkins CI pipeline

##### Explanation of backend(Node.js) `backendPipeline.groovy`
- Run CI/CD for backend service
```bash
# Checkout main branch of git repo
git checkout main 

# Install Dependencies
npm ci

# Run testing
npm test

# Build docker image

# Deploy to PROD

```

##### Explanation of frontend(ReactJS) `frontendPipeline.groovy`
- Run CI/CD for frontend service
```bash
# Checkout main branch of git repo
git checkout main 

# Install Dependencies
npm ci

# Run testing
npm test

# Build docker image

# Deploy to PROD

```