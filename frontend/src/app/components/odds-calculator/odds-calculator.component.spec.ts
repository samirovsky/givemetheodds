import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { OddsCalculatorComponent } from './odds-calculator.component';

describe('OddsCalculatorComponent', () => {
  let component: OddsCalculatorComponent;
  let fixture: ComponentFixture<OddsCalculatorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
            HttpClientModule,
          ],
      declarations: [ OddsCalculatorComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OddsCalculatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
