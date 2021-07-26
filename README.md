# AD-Tech mini

> 작고 소중한 광고플랫폼 프로젝트

딜리셔스 인턴십(2021.06.07 ~ 2021.08.06) 도중 진행한 프로젝트

## API Reference
```
/core/v1/creative
GET /
GET /{id}
POST /
PUT /{id}
DELETE /{id}
GET /pause/{id}
GET /restart/{id}

/dsp/v1/advertisement
GET /

/
GET /
GET /create
GET /detail/{id}
GET /advertise
```

## Directory Structure
```
.
├── java.net.deali.intern
│   ├── application
│   ├── domain
│   ├── infrastucture
│   │   ├── configuration
│   │   ├── exception
│   │   ├── repository
│   │   └── util
│   ├── presentation
│   │   ├── controller
│   │   ├── dto
│   │   └── interceptor
│   └── InternApplication.java
└── resources
    ├── static
    │   └── js
    ├── templates
    └── application.yml
```