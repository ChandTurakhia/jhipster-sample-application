import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AddressHeaderDetailComponent } from './address-header-detail.component';

describe('Component Tests', () => {
  describe('AddressHeader Management Detail Component', () => {
    let comp: AddressHeaderDetailComponent;
    let fixture: ComponentFixture<AddressHeaderDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AddressHeaderDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ addressHeader: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AddressHeaderDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AddressHeaderDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load addressHeader on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.addressHeader).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
