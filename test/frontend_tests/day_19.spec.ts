import { ComponentFixture, TestBed } from '@angular/core/testing';
// import { CustomersComponent } from './customers.component';
// import { Customer } from '../../types/Customer'; // Adjust the path accordingly
// import { Customer } from '../app/bank/types/Customer';
import { CustomerarrayComponent } from '../app/bank/components/customerarray/customerarray.component';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { CustomerTS } from '../app/bank/types/tstypes/Customerts';

describe('CustomersComponent', () => {
  let component: CustomerarrayComponent;
  let fixture: ComponentFixture<CustomerarrayComponent>;
  // let authService: AuthService;
  let httpTestingController: HttpTestingController;
  // let loginUrl = `${environment.apiUrl}`;
  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CustomerarrayComponent],
      imports: [HttpClientTestingModule]
    });

    fixture = TestBed.createComponent(CustomerarrayComponent);
    component = fixture.componentInstance;
    // authService = TestBed.inject(AuthService);
    // httpTestingController = TestBed.inject(HttpTestingController);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  

  it('should display customer information', () => {
    const customers: CustomerTS[] = [
      new CustomerTS("John Doe", "john@example.com", "john_doe", "password123", "User", "1"),
      new CustomerTS("John Doe1", "john1@example.com", "john_doe1", "password123", "Admin", "2")
    ];
  
    component.customers = customers;
    fixture.detectChanges();
  
    const compiled = fixture.nativeElement;
  
    // Check the presence of customer information in the compiled HTML
    expect(compiled.textContent).toContain('John Doe');
    expect(compiled.textContent).toContain('john@example.com');
    expect(compiled.textContent).toContain('john_doe');
    expect(compiled.textContent).toContain('John Doe1');
    expect(compiled.textContent).toContain('john1@example.com');
    expect(compiled.textContent).toContain('john_doe1');
  });

  
});

