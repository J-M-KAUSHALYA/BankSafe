import { TestBed, inject } from "@angular/core/testing";
import {
  HttpClientTestingModule,
  HttpTestingController,
} from "@angular/common/http/testing";
import { AuthService } from "../app/auth/services/auth.service";
// import { User } from "../app/auth/types/user";
import { environment } from "../environments/environment";
import { Customer } from "../app/bank/types/Customer"; 

describe("AuthService", () => {
  let authService: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService],
    });

    authService = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it("should be created", () => {
    expect(authService).toBeTruthy();
  });

  describe("login", () => {
    it("should make a POST request to login", () => {
      const user: Customer = new Customer({
        username: "admin.user",
        password: "12345",
        role: "ADMIN",
        name: "admin",
        email: "admin@gmail.com",
      });

      authService.login(user).subscribe((result) => {
        expect(result).not.toBeNull();
      });

      const req = httpMock.expectOne(`${environment.apiUrl}/customer/login`);
      expect(req.request.method).toBe("POST");
      expect(req.request.body).toEqual(user);

      req.flush(user);
    });
  });

  describe("getToken", () => {
    it("should get the token from localStorage", () => {
      const token = "testtoken";
      spyOn(localStorage, "getItem").and.returnValue(token);

      const result = authService.getToken();

      expect(localStorage.getItem).toHaveBeenCalledWith("token");
      expect(result).toEqual(token);
    });
  });

 

  describe("createUser", () => {
    it("should make a POST request to create a user", () => {
      const user: Customer = new Customer({
        username: "testuser",
        password: "testpassword",
        role: "USER",
        name:"test",
        email:"test@gmail.com",
      });
      const response: Customer = new Customer({
        username: "testuser",
        password: "testpassword",
        role: "USER",
        name:"test",
        email:"test@gmail.com"
      });

      authService.createUser(user).subscribe((result) => {
        expect(result).toEqual(response);
      });

      const req = httpMock.expectOne(`${environment.apiUrl}/customer/register`);
      expect(req.request.method).toBe("POST");
      expect(req.request.body).toEqual(user);

      req.flush(response);
    });
  });
});
