import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { of, throwError } from 'rxjs';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule, HttpTestingController } from "@angular/common/http/testing";
import { Router } from '@angular/router';
import { AccountComponent } from '../app/bank/components/account/account.component';
import { CustomersComponent } from '../app/bank/components/customer/customer.component';
import { DashboardComponent } from '../app/bank/components/dashboard/dashboard.component';
import { BankService } from '../app/bank/services/bank.service';
import { BankModule } from '../app/bank/bank.module';


class MockBankService {
    getAllCustomers() {
        return of([{ customerId: 1, name: 'John Doe' }]);
    }
    addAccount(account: any) {
        return of({ accountId: 1 });
    }
    addCustomer(customer: any) {
        return of({ customerId: 1});
    }
    getAllAccounts() {
        return of([{ accountId: 1, balance: 1000, customer: { customerId: 1, name: 'John Doe' } }]);
    }
    getAllTranactions() {
        return of([{ transactionId: 1, amount: 500 }]);
    }
}


describe('AccountComponent', () => {
    let bankService: BankService;
    let component: AccountComponent;
    let fixture: ComponentFixture<AccountComponent>;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [ReactiveFormsModule, FormsModule, ReactiveFormsModule, HttpClientTestingModule, RouterTestingModule.withRoutes([])],
            providers: [{ provide: BankService, useClass: MockBankService }],
        });
        bankService = TestBed.inject(BankService);
        fixture = TestBed.createComponent(AccountComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create AccountComponent', () => {
        expect(component).toBeTruthy();
    });

    it('should initialize customers on load', () => {
        spyOn(bankService, 'getAllCustomers').and.callThrough();
        component.loadCustomers();
        expect(bankService.getAllCustomers).toHaveBeenCalled();
        expect(component.customers.length).toBe(1);
    });

    it('should validate account form fields', () => {
        const balanceControl = component.accountForm.get('balance');
        balanceControl?.setValue(-1);
        expect(balanceControl?.hasError('min')).toBe(true);
    });

    it('should display success message on valid form submission', fakeAsync(() => {
        spyOn(bankService, 'addAccount').and.callThrough();
        component.accountForm.setValue({
            customer: 1,
            balance: 1000,
        });
        component.onSubmit();
        tick();
        expect(bankService.addAccount).toHaveBeenCalled();
        expect(component.successMessage).toBe('Account created successfully');
    }));

    it('should display error message on invalid form submission', () => {
        component.accountForm.setValue({
            customer: null,
            balance: -100,
        });
        component.onSubmit();
        expect(component.errorMessage).toBe('Please fill out all required fields correctly.');
    });
});

describe('CustomersComponent', () => {
    let bankService: BankService;
    let component: CustomersComponent;
    let fixture: ComponentFixture<CustomersComponent>;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [ReactiveFormsModule, FormsModule, ReactiveFormsModule, HttpClientTestingModule, RouterTestingModule.withRoutes([])],
            providers: [{ provide: BankService, useClass: MockBankService }],
        });
        bankService = TestBed.inject(BankService);
        fixture = TestBed.createComponent(CustomersComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create CustomersComponent', () => {
        expect(component).toBeTruthy();
    });

    it('should validate customer form fields', () => {
        const usernameControl = component.customerForm.get('username');
        usernameControl?.setValue('invalid$user');
        expect(usernameControl?.hasError('specialCharacters')).toBe(true);
    });

    it('should display success message on valid form submission', fakeAsync(() => {
        spyOn(bankService, 'addCustomer').and.callThrough();
        component.customerForm.setValue({
            name: 'John Doe',
            email: 'john@example.com',
            username: 'johndoe',
            password: 'Password123',
            role: 'USER',
        });
        component.onSubmit();
        tick();
        expect(bankService.addCustomer).toHaveBeenCalled();
        expect(component.customerSuccess).toBe('Customer created successfully');
    }));

    it('should display error message on invalid form submission', () => {
        component.customerForm.setValue({
            name: '',
            email: '',
            username: '',
            password: '',
            role: '',
        });
        component.onSubmit();
        expect(component.customerError).toBe('Please fill out all required fields correctly.');
    });
});

describe('DashboardComponent', () => {
    let bankService: BankService;
    let component: DashboardComponent;
    let fixture: ComponentFixture<DashboardComponent>;
    let router: Router;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [DashboardComponent],
            imports: [ReactiveFormsModule, HttpClientTestingModule, RouterTestingModule.withRoutes([]),BankModule],
            providers: [{ provide: BankService, useClass: MockBankService }],
        }).compileComponents();

        fixture = TestBed.createComponent(DashboardComponent);
        component = fixture.componentInstance;
        router = TestBed.inject(Router);
        bankService = TestBed.inject(BankService);
        fixture.detectChanges();
    });

    it('should create DashboardComponent', () => {
        expect(component).toBeTruthy();
    });

    it('should load admin data if role is ADMIN', () => {
        spyOn(component, 'loadAdminData').and.callThrough();
        localStorage.setItem('role', 'ADMIN');
        component.ngOnInit();
        expect(component.loadAdminData).toHaveBeenCalled();
    });

    it('should load customers, accounts, and transactions', fakeAsync(() => {
        spyOn(bankService, 'getAllCustomers').and.callThrough();
        spyOn(bankService, 'getAllAccounts').and.callThrough();
        spyOn(bankService, 'getAllTranactions').and.callThrough();

        component.loadAdminData();
        tick();

        expect(bankService.getAllCustomers).toHaveBeenCalled();
        expect(bankService.getAllAccounts).toHaveBeenCalled();
        expect(bankService.getAllTranactions).toHaveBeenCalled();

        expect(component.customers.length).toBe(1);
        expect(component.accounts.length).toBe(1);
        expect(component.transactions.length).toBe(1);
    }));
});
