import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StatsDelayTimesComponent } from './stats-delay-times.component';

describe('StatsDelayTimesComponent', () => {
  let component: StatsDelayTimesComponent;
  let fixture: ComponentFixture<StatsDelayTimesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StatsDelayTimesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StatsDelayTimesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
