import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StatsEditComponent } from './stats-edit.component';

describe('StatsEditComponent', () => {
  let component: StatsEditComponent;
  let fixture: ComponentFixture<StatsEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StatsEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StatsEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
