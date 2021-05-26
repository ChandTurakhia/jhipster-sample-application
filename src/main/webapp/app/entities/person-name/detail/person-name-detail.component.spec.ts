import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PersonNameDetailComponent } from './person-name-detail.component';

describe('Component Tests', () => {
  describe('PersonName Management Detail Component', () => {
    let comp: PersonNameDetailComponent;
    let fixture: ComponentFixture<PersonNameDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PersonNameDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ personName: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PersonNameDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PersonNameDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load personName on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.personName).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
