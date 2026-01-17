import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { EditAccountComponent } from '../app/bank/components/accountedit/accountedit.component';
import { EditCustomerComponent } from '../app/bank/components/customeredit/customeredit.component';
import { BankService } from '../app/bank/services/bank.service';
import { Account } from '../app/bank/types/Account';
import { Customer } from '../app/bank/types/Customer';


class MockBankService {
    getAccountById(accountId: number) {
        return of({ accountId: 1, customer: { customerId: 1, name: 'John Doe', email: 'john@gmail.com', username: 'john', password: 'password', role: 'USER ' }, balance: 1000 });
    }
    getAllCustomers() {
        return of([{ customerId: 1, name: 'John Doe', email: 'john@gmail.com', username: 'john', password: 'password', role: 'USER ' }]);
    }
    editAccount(account: Account) {
        return of(account);
    }
    getCustomerById(customerId: number) {
        return of({ customerId: 1, name: 'John Doe', email: 'john@gmail.com', username: 'john', password: 'password', role: 'USER ' });
    }
    editCustomer(customer: Customer) {
        return of(customer);
    }
}

describe('Edit Components', () => {
    let bankService: BankService;
    let router: Router;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [ReactiveFormsModule],
            declarations: [EditAccountComponent, EditCustomerComponent],
            providers: [
                { provide: BankService, useClass: MockBankService },
                { provide: ActivatedRoute, useValue: { params: of({ accountId: 1, customerId: 1 }) } },
                { provide: Router, useValue: { navigate: jasmine.createSpy('navigate') } },
            ],
        }).compileComponents();

        bankService = TestBed.inject(BankService);
        router = TestBed.inject(Router);
    });

    describe('EditAccountComponent', () => {
        let component: EditAccountComponent;
        let fixture: ComponentFixture<EditAccountComponent>;

        beforeEach(() => {
            fixture = TestBed.createComponent(EditAccountComponent);
            component = fixture.componentInstance;
            fixture.detectChanges();
        });

        it('should create the component', () => {
            expect(component).toBeTruthy();
        });

        it('should initialize the form on ngOnInit', () => {
            expect(component.accountForm).toBeDefined();
            expect(component.accountForm.get('customer')).toBeTruthy();
            expect(component.accountForm.get('balance')).toBeTruthy();
        });

        it('should load account details on initialization', fakeAsync(() => {
            spyOn(bankService, 'getAccountById').and.callThrough();
            component.ngOnInit();
            tick();
            expect(bankService.getAccountById).toHaveBeenCalledWith(1);
            expect(component.account?.accountId).toBe(1);
        }));

        it('should handle form submission successfully', fakeAsync(() => {
            spyOn(bankService, 'editAccount').and.callThrough();
            component.accountForm.setValue({ customer: 1, balance: 2000 });
            component.onSubmit();
            tick();
            expect(bankService.editAccount).toHaveBeenCalled();
            expect(component.successMessage).toBe('Account updated successfully');
            expect(router.navigate).toHaveBeenCalledWith(['/bank']);
        }));

    });

    describe('EditCustomerComponent', () => {
        let component: EditCustomerComponent;
        let fixture: ComponentFixture<EditCustomerComponent>;

        beforeEach(() => {
            fixture = TestBed.createComponent(EditCustomerComponent);
            component = fixture.componentInstance;
            fixture.detectChanges();
        });

        it('should create the component', () => {
            expect(component).toBeTruthy();
        });

        it('should initialize the form on ngOnInit', () => {
            expect(component.customerForm).toBeDefined();
            expect(component.customerForm.get('name')).toBeTruthy();
            expect(component.customerForm.get('username')).toBeTruthy();
            expect(component.customerForm.get('password')).toBeTruthy();
        });

        it('should load customer details on initialization', fakeAsync(() => {
            spyOn(bankService, 'getCustomerById').and.callThrough();
            component.ngOnInit();
            tick();
            expect(bankService.getCustomerById).toHaveBeenCalledWith(1);
        }));

        it('should handle form submission successfully', fakeAsync(() => {
            spyOn(bankService, 'editCustomer').and.callThrough();
            component.customerForm.setValue({
                name: 'John Doe',
                username: 'johndoe',
                password: 'Password123',
                role: 'USER',
                email: 'john@gmail.com'
            });
            component.onSubmit();
            tick();
            expect(bankService.editCustomer).toHaveBeenCalled();
            expect(component.customerSuccess).toBe('Customer updated successfully');
            expect(router.navigate).toHaveBeenCalledWith(['/bank']);
        }));

    });
});
