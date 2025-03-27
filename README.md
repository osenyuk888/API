# REST Assured API Test Automation Architecture
Ready-to-use API Test Automation Architecture using Java and REST Assured.

## Prerequisites
Before you start, make sure you have the following installed:
1. Java 17
2. Maven
3. Allure
4. Git
5. IntelliJ IDEA (recommended)

## Installation Steps
1. [Fork](https://github.com/osenyuk888/API/fork) repository.
2. Import the project as a Maven project in IntelliJ IDEA.
3. Clone or download the repository to your local machine:
```bash
  git clone https://github.com/[your_username]/API.git
```
4. Install dependencies by running the script. This command downloads and installs all required dependencies without running tests.
```bash
  mvn install -DskipTests
```  
5. Run tests using
```bash
  mvn clean test
```  
6. Generate the test report using the following command.
```bash
 allure serve target/allure-results
```

## Languages and Frameworks

The project uses the following:
- Java 17 – programming language.
- REST Assured – HTTP client for API testing.
- Gson – JSON parser.
- TestNG – testing framework.
- Lombok – generates getters, setters, builders, and more.
- Owner – simplifies handling of properties files.
- Allure Report – test reporting framework.
- Maven – build automation tool.
- IntelliJ IDEA – preferred IDE.

  ## Project structure
```bash
📦 API
|   .gitignore
|   pom.xml
|   README.md
|   structure.txt
|   
+---.github
|   \---workflows
|           maven.yml
|           
\---src
    +---main
    |   +---java
    |      \---api
    |          +---clients
    |          |       AuthorsClient.java
    |          |       BooksClient.java
    |          |       Client.java
    |          |       
    |          +---config
    |          |       Configuration.java
    |          |       ConfigurationManager.java
    |          |       
    |          +---data
    |          |   \---providers
    |          |           TestDataProvider.java
    |          |           
    |          +---listeners
    |          |       TestListener.java
    |          |       
    |          +---pojo
    |          |       Author.java
    |          |       Book.java
    |          |       
    |          \---support
    |                  AuthorMatcher.java
    |                  AuthorUtils.java
    |                  BookUtils.java                      
    |                           
    \---test
        +---java
        |   \---testng
        |       \---api
        |           \---tests
        |                   AuthorsControllerTests.java
        |                   BaseApiTest.java
        |                   BooksControllerTests.java
        |                   
        \---resources
            |   allure.properties
            |   config.properties
            |   suite.xml
            |   
            \---json
                \---schemas
                    +---authors
                    |       author.json
                    |       authors.json
                    |       
                    \---books
                            book.json
                            books.json
```
