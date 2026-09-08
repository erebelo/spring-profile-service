# Spring Profile Service

REST API developed with Java 21 and Spring Boot 4..

## Requirements

- Java 21
- Spring Boot 4.x.x
- Apache Maven 3.8.6

## Libraries

- [spring-common-parent](https://github.com/erebelo/spring-common-parent): Manages the Spring Boot version and provide common configurations for
  plugins and formatting.

## Configuring Maven for GitHub Dependencies

To pull the `spring-common-parent` dependency, follow these steps:

1. **Generate a Personal Access Token**:

   Go to your GitHub account -> **Settings** -> **Developer settings** -> **Personal access tokens** -> **Tokens (classic)** -> **Generate new token (
   classic)**:
   - Fill out the **Note** field: `Pull packages`.
   - Set the scope:
     - `read:packages` (to download packages)
   - Click **Generate token**.

2. **Set Up Maven Authentication**:

   In your local Maven `settings.xml`, define the GitHub repository authentication using the following structure:

   ```xml
   <servers>
     <server>
       <id>github-spring-common-parent</id>
       <username>USERNAME</username>
       <password>TOKEN</password>
     </server>
   </servers>
   ```

   **NOTE**: Replace `USERNAME` with your GitHub username and `TOKEN` with the personal access token you just generated.

## Run App

- Run the `SpringProfileServiceApplication` class as Java Application.

## Database Setup

Create the `profile_db` database and the required collections and indexes.

**Create database:**

```javascript
use profile_db
```

## Collection

[Project Collection](https://github.com/erebelo/spring-profile-service/tree/main/collection)
