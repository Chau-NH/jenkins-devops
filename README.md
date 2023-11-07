# jenkins-devpos
Apply a Shared Library to Jenkins CI pipeline

#### Explanation of backend(Node.js) `backendPipeline.groovy`
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

#### Explanation of frontend(ReactJS) `frontendPipeline.groovy`
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

#### Install Jenkins on AWS EC2
Step to install a Jenkins server on EC2
- Step 1: Install Jenkins\
	https://www.jenkins.io/doc/tutorials/tutorial-for-installing-jenkins-on-AWS/
	
- Step 2 : Install Docker\
 	https://www.cyberciti.biz/faq/how-to-install-docker-on-amazon-linux-2 \
	SET perssmion for docker.sock: \
		```
		sudo chmod 666 /var/run/docker.sock
  		```
	
- Step 3 : Install Kubectl\
	https://docs.aws.amazon.com/eks/latest/userguide/install-kubectl.html\
	
- Step 4 : Install npm and node\
	https://docs.aws.amazon.com/sdk-for-javascript/v2/developer-guide/setting-up-node-on-ec2-instance.html\
	https://linux.how2shout.com/how-to-install-node-and-npm-on-amazon-linux-2-aws-ec2 \
	Run command to install
  		```
		sudo yum install nodejs npm
  		```

- Step 5 : Intall git if not
	``` 
 	sudo yum install git -y
 	```
- Step 6 : Install Trivy
	https://aquasecurity.github.io/trivy/v0.29.2/getting-started/installation/
	
- Step 7 : Access to Jenkins server\
	Install AWS Pipeline plugin --> to access to aws resource\
	Install Kubenetes Plugin
	HTML Publisher plugin


### Set up for HTML Publisher plugin to load CSS and JS

1. Open the Jenkin home page.
2. Go to Manage Jenkins.
3. Now go to Script Console.
4. And in that console paste below statement and click on Run.
```
System.setProperty("hudson.model.DirectoryBrowserSupport.CSP", "")
```

