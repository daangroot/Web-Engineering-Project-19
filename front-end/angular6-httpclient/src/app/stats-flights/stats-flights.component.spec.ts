import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StatsFlightsComponent } from './stats-flights.component';

describe('StatsFlightsComponent', () => {
  let component: StatsFlightsComponent;
  let fixture: ComponentFixture<StatsFlightsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StatsFlightsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StatsFlightsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
