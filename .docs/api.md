# API Architecture

This architecture provides a clean separation of concerns for handling API requests, especially those requiring authentication.

## Components

### 1. API Service
`ApiService` interface defines all API endpoints the app can access.

```kotlin
interface ApiService {
    // Auth endpoints (no authentication required)
    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
    
    // Protected endpoints (require authentication)
    @GET("api/v1/users/{userId}/profile")
    suspend fun getUserProfile(@Path("userId") userId: String): Profile
}
```

### 2. Auth Interceptor
`AuthInterceptor` automatically adds the auth token to all API requests.

```kotlin
class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        
        return if (token != null) {
            val newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
            chain.proceed(newRequest)
        } else {
            chain.proceed(originalRequest)
        }
    }
}
```

### 3. Network Module
`NetworkModule` configures and provides API clients with proper authentication.

```kotlin
object NetworkModule {
    private const val BASE_URL = "http://10.0.2.2:8081/"
    
    private var apiService: ApiService? = null
    
    fun provideApiService(context: Context): ApiService {
        if (apiService == null) {
            val client = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(context))
                .build()
                
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                
            apiService = retrofit.create(ApiService::class.java)
        }
        
        return apiService!!
    }
}
```

### 4. Repositories
Repositories handle business logic and API calls for specific domains.

**AuthRepository:**
```kotlin
class AuthRepository(
    private val apiService: ApiService,
    private val context: Context
) {
    suspend fun login(username: String, password: String): Result<Boolean> {
        // Implementation
    }
    
    fun isLoggedIn(): Boolean {
        // Implementation
    }
    
    fun logout() {
        // Implementation
    }
}
```

**UserRepository:**
```kotlin
class UserRepository(private val apiService: ApiService) {
    suspend fun getUserProfile(userId: String): Result<Profile> {
        // Implementation
    }
    
    fun getCurrentUserId(context: Context): String? {
        // Implementation
    }
}
```

## How to Use This Architecture

### Step 1: Authentication
Authentication is handled by the `AuthRepository`. When a user logs in, the token is automatically stored in SharedPreferences.

```kotlin
val apiService = NetworkModule.provideApiService(context)
val authRepository = AuthRepository(apiService, context)

// Login and store token
viewModelScope.launch {
    val result = authRepository.login(username, password)
    result.fold(
        onSuccess = { /* Handle success */ },
        onFailure = { /* Handle failure */ }
    )
}
```

### Step 2: Making Authenticated Requests
Once logged in, all requests through the API client will automatically include the auth token.

```kotlin
val userRepository = UserRepository.getInstance(context)
viewModelScope.launch {
    userRepository.getUserProfile(userId).fold(
        onSuccess = { profile -> /* Use profile data */ },
        onFailure = { exception -> /* Handle error */ }
    )
}
```

### Step 3: Logout
When logging out, simply call the logout method:

```kotlin
authRepository.logout()
```

## Example ViewModel

```kotlin
class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState
    
    fun loadUserProfile(context: Context) {
        val repository = UserRepository.getInstance(context)
        val userId = repository.getCurrentUserId(context)
        
        viewModelScope.launch {
            repository.getUserProfile(userId).fold(
                onSuccess = { profile ->
                    _uiState.value = _uiState.value.copy(profile = profile)
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(error = exception.message)
                }
            )
        }
    }
}
``` 