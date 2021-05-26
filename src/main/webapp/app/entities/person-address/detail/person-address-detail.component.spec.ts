import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PersonAddressDetailComponent } from './person-address-detail.component';

describe('Component Tests', () => {
  describe('PersonAddress Management Detail Component', () => {
    let comp: PersonAddressDetailComponent;
    let fixture: ComponentFixture<PersonAddressDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PersonAddressDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ personAddress: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PersonAddressDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PersonAddressDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load personAddress on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.personAddress).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
