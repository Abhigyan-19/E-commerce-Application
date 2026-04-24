📘 E-Commerce User Management API (Spring Boot)
🔷 Project Overview

This project is a User Management REST API built using:

Spring Boot
Spring Data JPA (Hibernate)
Lombok
MySQL / H2 (assumed DB)

It supports:

Creating users
Fetching all users
Fetching user by ID
Updating users
🏗️ Project Architecture
Controller → Service → Repository → Database
Flow:
Client (JSON Request)
        ↓
Controller (API Layer)
        ↓
Service (Business Logic + Mapping)
        ↓
Repository (DB Access)
        ↓
Database
📁 Project Structure
com.ecom
 ├── controller       → REST APIs
 ├── service          → Business logic
 ├── repository       → Database access
 ├── model            → Entities (DB structure)
 ├── dto              → Request/Response objects
 └── ECommerceApplication.java
🧠 Core Concept: DTO vs Entity
🔴 Entity (User, Address)
Represents database structure
Used by JPA/Hibernate
🟢 DTOs
UserRequest → Input from client
UserResponse → Output to client
AddressDTO → Nested object transfer
🔁 Data Flow
UserRequest → User → UserResponse
📦 ENTITY LAYER
🔹 User.java
@Entity(name = "user_table")
Key fields:
id → auto-generated
firstName, lastName, email, phone
role → default CUSTOMER
address → One-to-One mapping
createdAt, updatedAt → auto timestamps
🔹 Address Mapping
@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
@JoinColumn(name ="address_id", referencedColumnName = "id")
private Address address;
Meaning:
One user → One address
cascade = ALL → saving user saves address
orphanRemoval = true → deleting user removes address
🔹 Address.java

Represents address table:

street, city, state, country, zipcode
🔹 UserRole.java
CUSTOMER, ADMIN
📦 DTO LAYER
🔹 UserRequest
private String firstName;
private String lastName;
private String email;
private String phone;
private AddressDTO address;

👉 Used when client sends data

🔹 UserResponse
private Long id;
private String firstName;
private String lastName;
private String email;
private String phone;
private UserRole role;
private AddressDTO address;

👉 Used when sending response to client

🔹 AddressDTO

Same structure as Address but used for safe data transfer.

📦 REPOSITORY LAYER
🔹 UserRepository
public interface UserRepository extends JpaRepository<User, Long>
Provides:
findAll()
findById()
save()
deleteById()
📦 CONTROLLER LAYER
🔹 Base URL
/api/users
🔹 1. Get All Users
@GetMapping
public ResponseEntity<List<UserResponse>> getAllUsers()
Flow:
Controller → Service → Repository → DB
                ↓
        List<User>
                ↓ (mapping)
        List<UserResponse>
🔹 2. Get User by ID
@GetMapping("/{id}")
public ResponseEntity<UserResponse> getUser(@PathVariable Long id)
Important concept: Optional
return userService.fetchUser(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
Meaning:
If user exists → return 200 OK
Else → return 404 NOT FOUND
🔹 3. Create User
@PostMapping
public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest)
Flow:
UserRequest → User → Save to DB
🔹 4. Update User
@PutMapping("/{id}")
public ResponseEntity<String> updateUser(...)
Logic:
If user exists → update and return success
Else → return 404
⚙️ SERVICE LAYER (CORE LOGIC)
🔹 1. Fetch All Users
Problem earlier:
return userRepository.findAll(); ❌

Error:

List<User> ≠ List<UserResponse>
✅ Solution:
return userRepository.findAll()
        .stream()
        .map(this::mapToUserResponse)
        .collect(Collectors.toList());
🔹 2. Fetch User by ID
Problem:
Optional<User> ≠ Optional<UserResponse>
✅ Solution:
return userRepository.findById(id)
        .map(this::mapToUserResponse);
🔹 3. Mapping Method
private UserResponse mapToUserResponse(User user)
Purpose:

Convert:

User → UserResponse
Key Learning:
Simple fields → direct copy
Nested object → manual mapping
if (user.getAddress() != null) {
    AddressDTO dto = new AddressDTO();
    ...
    response.setAddress(dto);
}
🔹 4. Create User
public void addUser(UserRequest userRequest)
Flow:
Create empty User
        ↓
Fill using DTO
        ↓
Save using repository
🔹 5. updateUserFromRequest()
private void updateUserFromRequest(User user, UserRequest userRequest)
Purpose:

Convert:

UserRequest → User
⚠️ Important Concept
Why setAddress() is required?
Address address = new Address();

👉 Creates new object

But without:

user.setAddress(address);

👉 User will NOT contain address

Key Difference:
Type	Behavior
String fields	just update
Object fields	create + attach
🔥 KEY CONCEPTS YOU LEARNED
1. Type Safety
List<User> ≠ List<UserResponse>
Optional<User> ≠ Optional<UserResponse>
2. Mapping Required
Entity → DTO conversion must be explicit
3. Streams
.stream().map().collect()

Used for:

List<User> → List<UserResponse>
4. Optional
Optional<User>

Avoids:

NullPointerException
5. DTO Advantage
Benefit	Explanation
Security	Hide internal fields
Flexibility	Change DB without breaking API
Clean code	Separation of concerns
📡 API EXAMPLES
🔹 Create User
POST /api/users

{
  "firstName": "Rinju",
  "lastName": "Mukherjee",
  "email": "rinju@email.com",
  "phone": "1234567890",
  "address": {
    "street": "ABC Road",
    "city": "Kolkata",
    "state": "WB",
    "country": "India",
    "zipcode": "700001"
  }
}
🔹 Response
"User added successfully"
🔹 Get All Users
GET /api/users

Response:

[
  {
    "id": 1,
    "firstName": "Rinju",
    "lastName": "Mukherjee"
  }
]
🚀 FUTURE IMPROVEMENTS
Use MapStruct for automatic mapping
Add Validation (@Valid, @NotNull)
Add Exception Handling (@ControllerAdvice)
Add Pagination
Add Authentication (JWT / Spring Security)
🎯 FINAL SUMMARY
Client Request
    ↓
UserRequest (DTO)
    ↓
Service Layer (Mapping)
    ↓
User Entity
    ↓
Repository → Database
    ↓
UserResponse (DTO)
    ↓
Client Response
💡 Learning Journey 



DTO migration
Type mismatch errors
Stream mapping
Optional handling
Entity vs DTO separation

