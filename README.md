# TalibHub

<div align="center">
  <img width="685" alt="image" src="https://github.com/user-attachments/assets/426c45f5-982a-42ce-852e-0441a405680a" />
  <h3>A Digital Academic Social Network for Student Professionals</h3>
</div>

## 📚 Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Technologies](#technologies)
- [Architecture](#architecture)
- [Screenshots](#screenshots)
- [GitFlow Methodology](#gitflow-methodology)
- [Project Backlog](#project-backlog)
- [Installation & Setup](#installation--setup)
- [Team](#team)

## 🌟 Overview

TalibHub is a comprehensive digital platform designed specifically for university students and graduates. It serves as a professional social network where students can build their academic and professional profile, share experiences, connect with companies, and access job opportunities.

The platform creates a bridge between academic institutions and businesses, enabling students to showcase their skills while providing companies with access to talented candidates.

## ✨ Features

### 👤 User Profiles
- Professional profile creation with personal information
- Academic history and achievements
- Work experience and internships
- Skills and certifications
- Portfolio showcasing projects

### 🏢 Company Reviews
- Rate and review companies where students have interned or worked
- View detailed company profiles with overall ratings
- Read experiences from peers who worked at specific companies

### 💬 Social Feed
- Share posts and updates
- Engage with content through comments
- Stay updated with academic and professional news

### 🔍 Search Functionality
- Find other students by name, skills, or institution
- Discover companies and their reviews
- Search through posts and content

### 👨‍💼 Admin Features
- User management through CSV imports
- Account activation and moderation
- Platform analytics and reporting

## 🛠️ Technologies

```mermaid
mindmap
  root((TalibHub Tech))
    Backend
        Java 17
        Spring Boot 3.2.0
        Spring Security
        JWT Authentication
        PostgreSQL
        JPA/Hibernate
        Maven
    Frontend
        Angular 17
        TypeScript
        TailwindCSS
        RxJS
        Angular Material
    DevOps
        Docker
        GitHub Actions
        GitFlow
        CI/CD Pipeline
    Testing
        JUnit 5
        Mockito
        Karma
        Jasmine
```

## 🏗️ Architecture

TalibHub follows a clean, modern architecture with clear separation of concerns:

```
├── Backend (Spring Boot)
│   ├── Controllers - API endpoints
│   ├── Services - Business logic
│   ├── Models - Data entities
│   ├── Repositories - Data access
│   ├── DTOs - Data transfer objects
│   ├── Security - Authentication & authorization
│   └── Config - Application configuration
│
├── Frontend (Angular)
│   ├── Components - UI elements
│   ├── Services - Backend communication
│   ├── Models - Data structures
│   ├── Pages - Screen layouts
│   ├── Guards - Route protection
│   └── Interceptors - HTTP request handling
```

### System Architecture Diagram

```mermaid
graph TD
    Client[Client Browser] -->|HTTP/HTTPS| LB[Load Balancer]
    LB --> WebServer[Angular Frontend]
    WebServer -->|API Calls| API[Spring Boot Backend API]
    API -->|Authentication| Auth[Authentication Service]
    API -->|Data Access| DB[(PostgreSQL Database)]
    API -->|File Storage| FileStorage[File Storage Service]
    API -->|Email| EmailService[Email Service]
    
    subgraph "Frontend Services"
        WebServer --> Components[Angular Components]
        WebServer --> Services[Angular Services]
        WebServer --> Guards[Route Guards]
    end
    
    subgraph "Backend Services"
        API --> UserService[User Service]
        API --> ProfileService[Profile Service]
        API --> PostService[Post Service]
        API --> ReviewService[Review Service]
        API --> SearchService[Search Service]
    end
```

### Database Schema

```mermaid
erDiagram
    USER {
        string id PK
        string email
        string password
        string firstName
        string lastName
        string phone
        string imageUri
        string cin
        boolean enabled
        datetime createdAt
        datetime updatedAt
    }
    
    STUDENT {
        string id PK "Inherits from USER"
        string cne
        date birthDate
        year enrollmentYear
        year graduationYear
    }
    
    MANAGER {
        string id PK "Inherits from USER"
    }
    
    PROFILE {
        string id PK
        string about
    }
    
    EDUCATION {
        string id PK
        string title
        string studyField
        string description
        yearMonth startAt
        yearMonth endAt
        string location
    }
    
    EXPERIENCE {
        string id PK
        string title
        string description
        yearMonth startAt
        yearMonth endAt
        string location
    }
    
    INSTITUT {
        string id PK
        string name
        string website
        string imageUri
    }
    
    POSTE {
        string id PK
        string titre
        string description
        string imageUri
        datetime createdOn
        datetime lastUpdatedOn
    }
    
    COMMENT {
        string id PK
        string content
        datetime createdOn
        datetime lastUpdatedOn
    }
    
    REVIEW {
        string id PK
        string review
        int rating
        datetime createdOn
    }
    
    SESSION {
        string id PK
        string token
        datetime createdAt
        datetime lastRefreshedAt
        datetime expiresAt
    }
    
    STUDENT ||--o| PROFILE : has
    PROFILE ||--o{ EDUCATION : contains
    PROFILE ||--o{ EXPERIENCE : contains
    EDUCATION }o--|| INSTITUT : belongs_to
    EXPERIENCE }o--|| INSTITUT : belongs_to
    USER ||--o{ POSTE : creates
    USER ||--o{ COMMENT : writes
    STUDENT ||--o{ REVIEW : writes
    POSTE ||--o{ COMMENT : has
    COMMENT ||--o{ COMMENT : has_replies
    INSTITUT ||--o{ REVIEW : receives
    USER ||--o{ SESSION : has
```

## 📸 Screenshots

### Key Application Views

The application consists of several key screens that provide the core functionality:

1. **Login Page**: Secure authentication with email and password
2. **Home Feed**: Social posts from connections and educational institutions
3. **Student Profile**: Comprehensive view of academic and professional information
4. **Company Reviews**: Ratings and feedback for employers
5. **Admin Dashboard**: User management and platform administration

Screenshots will be added as the application development progresses. The current UI follows a clean, modern design with the TalibHub color scheme.

## 🔀 GitFlow Methodology

The TalibHub project follows the GitFlow workflow model to ensure structured development and deployment:

```mermaid
gitGraph
    commit id: "initial"
    branch develop
    checkout develop
    commit id: "setup project structure"
    branch feature/user-auth
    checkout feature/user-auth
    commit id: "implement user auth"
    commit id: "add JWT tokens"
    checkout develop
    merge feature/user-auth
    branch feature/profiles
    checkout feature/profiles
    commit id: "add user profiles"
    commit id: "education sections"
    commit id: "experience sections"
    checkout develop
    merge feature/profiles
    branch release/1.0.0
    checkout release/1.0.0
    commit id: "prepare 1.0.0"
    checkout main
    merge release/1.0.0 tag: "v1.0.0"
    checkout develop
    merge release/1.0.0
    branch feature/posts
    checkout feature/posts
    commit id: "implement posts"
    checkout develop
    merge feature/posts
    branch feature/comments
    checkout feature/comments
    commit id: "add comments functionality"
    checkout develop
    merge feature/comments
    branch hotfix/security-issue
    checkout hotfix/security-issue
    commit id: "fix token validation"
    checkout main
    merge hotfix/security-issue tag: "v1.0.1"
    checkout develop
    merge hotfix/security-issue
    branch release/1.1.0
    checkout release/1.1.0
    commit id: "prepare 1.1.0"
    checkout main
    merge release/1.1.0 tag: "v1.1.0"
    checkout develop
    merge release/1.1.0
```

### Branch Structure

- **`main`**: Production-ready code, always stable
- **`develop`**: Integration branch for features, serves as the pre-production environment
- **`feature/*`**: Individual feature development branches
- **`release/*`**: Branches for release preparation
- **`hotfix/*`**: Emergency bug fixes for production issues

### Development Workflow

1. **Feature Development**:
   - Create a feature branch from `develop`: `git checkout -b feature/new-feature develop`
   - Implement changes with regular commits
   - When complete, merge back to `develop`: 
     ```
     git checkout develop
     git merge --no-ff feature/new-feature
     git push origin develop
     ```

2. **Release Preparation**:
   - Create a release branch when ready: `git checkout -b release/1.0.0 develop`
   - Prepare the release (version bumps, last fixes)
   - Merge to both `main` and `develop` when ready:
     ```
     git checkout main
     git merge --no-ff release/1.0.0
     git tag -a 1.0.0
     git checkout develop
     git merge --no-ff release/1.0.0
     ```

3. **Hotfixes**:
   - For production issues, create a hotfix branch: `git checkout -b hotfix/bug-fix main`
   - Implement the fix
   - Merge to both `main` and `develop`:
     ```
     git checkout main
     git merge --no-ff hotfix/bug-fix
     git tag -a 1.0.1
     git checkout develop
     git merge --no-ff hotfix/bug-fix
     ```

### CI/CD Pipeline

The project uses GitHub Actions for continuous integration and deployment, with separate workflows for the backend and frontend components.

```mermaid
graph TD
    PR[Pull Request Created] --> CI[CI Pipeline Triggered]
    Push[Push to develop/main] --> CI
    CI --> Test[Run Tests]
    Test --> Build[Build Application]
    Build --> Deploy[Deploy to Environment]
    Deploy -->|On develop| Staging[Staging Environment]
    Deploy -->|On main| Production[Production Environment]
    
    subgraph "Quality Gates"
        Test --> UnitTests[Unit Tests]
        Test --> CodeCoverage[Code Coverage]
        Test --> StaticAnalysis[Static Code Analysis]
    end
    
    subgraph "Build Process"
        Build --> CompileBackend[Compile Java Code]
        Build --> CompileFrontend[Build Angular App]
        Build --> CreateArtifacts[Create Deployment Artifacts]
    end
```

## 📋 Project Backlog

```mermaid
gantt
    title TalibHub Development Roadmap
    dateFormat  YYYY-MM-DD
    
    section Phase 1 - Core
    User Authentication              :done, auth, 2024-01-01, 2024-01-15
    Student Profiles                 :done, profiles, 2024-01-16, 2024-02-01
    Education & Experience Sections  :done, sections, 2024-02-02, 2024-02-15
    Post Creation & Feed             :done, posts, 2024-02-16, 2024-03-01
    Comments System                  :done, comments, 2024-03-02, 2024-03-15
    Admin Dashboard                  :done, admin, 2024-03-16, 2024-03-31
    Company Reviews                  :done, reviews, 2024-04-01, 2024-04-15
    Basic Search                     :done, search, 2024-04-16, 2024-04-30
    
    section Phase 2 - Enhanced Features
    Notifications System            :active, notif, 2024-05-01, 2024-05-15
    Direct Messaging                :active, msg, 2024-05-16, 2024-05-31
    Enhanced Admin Analytics        :active, analytics, 2024-06-01, 2024-06-15
    
    section Phase 3 - Advanced Features
    Job Board Integration           :job, 2024-06-16, 2024-06-30
    Event Calendar                  :calendar, 2024-07-01, 2024-07-15
    Skills Endorsement              :skills, 2024-07-16, 2024-07-31
    Mobile Application              :mobile, 2024-08-01, 2024-08-31
    Advanced Search                 :advsearch, 2024-09-01, 2024-09-15
    Recommendation Engine           :rec, 2024-09-16, 2024-10-31
```

### Feature Status

#### Completed ✅
- User authentication and authorization system
- Student profile creation and management
- Education and experience sections for profiles
- Post creation and feed display
- Comments on posts
- Admin dashboard for student management
- Company reviews system
- Search functionality for students

#### In Progress 🔄
- Notifications system
- Direct messaging between users
- Enhanced analytics for admins

#### Planned 📌
- Job board integration
- Event calendar
- Skills endorsement system
- Mobile application
- Advanced search with filters
- Recommendation engine

## 🚀 Installation & Setup

### Prerequisites
- Java 17
- Node.js 16+
- PostgreSQL
- Maven

### Backend Setup

```bash
# Clone the repository
git clone https://github.com/yourusername/talibhub.git
cd talibhub

# Navigate to backend directory
cd backend

# Install dependencies and build
mvn clean install

# Run the application
mvn spring-boot:run
```

### Frontend Setup

```bash
# Navigate to frontend directory
cd ../frontend

# Install dependencies
npm install

# Run the application
ng serve
```

The application will be available at:
- Frontend: http://localhost:4200
- Backend API: http://localhost:8081

### Docker Setup (Alternative)

```bash
# Build and run with Docker Compose
docker-compose up -d
```

## 👥 Team

### Contributors

- **Walid Ahdouf**: [Walid Ahdouf](https://github.com/w0l1d)
- **Abderrahman AITRAIS**: [Abderrahmane Ait Rais](https://github.com/AbdoAitrais)
- **Anas KABILA**: [Anas KABILA](https://github.com/KABILA-Anas)
- **Yassine JRAYFY**: [YASSINE Jrayfy](https://github.com/YASSINEJR3)


The team follows a collaborative approach where each member contributes across different aspects of the project while maintaining their area of expertise.

---

<div align="center">
  <p>© 2025 TalibHub Team. All rights reserved.</p>
</div>


==================================================
