import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DelayTimeStatisticsComponent } from './delay-time-statistics.component';

describe('DelayTimeStatisticsComponent', () => {
  let component: DelayTimeStatisticsComponent;
  let fixture: ComponentFixture<DelayTimeStatisticsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DelayTimeStatisticsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DelayTimeStatisticsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
