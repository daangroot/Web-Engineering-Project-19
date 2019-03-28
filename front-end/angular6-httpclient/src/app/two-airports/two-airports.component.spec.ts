import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TwoAirportsComponent } from './two-airports.component';

describe('TwoAirportsComponent', () => {
  let component: TwoAirportsComponent;
  let fixture: ComponentFixture<TwoAirportsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TwoAirportsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TwoAirportsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
