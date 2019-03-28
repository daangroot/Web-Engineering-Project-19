import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExtrastatsCarrierComponent } from './extrastats-carrier.component';

describe('ExtrastatsCarrierComponent', () => {
  let component: ExtrastatsCarrierComponent;
  let fixture: ComponentFixture<ExtrastatsCarrierComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExtrastatsCarrierComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExtrastatsCarrierComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
