import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PersonDetailsDetailComponent } from './person-details-detail.component';

describe('Component Tests', () => {
  describe('PersonDetails Management Detail Component', () => {
    let comp: PersonDetailsDetailComponent;
    let fixture: ComponentFixture<PersonDetailsDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PersonDetailsDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ personDetails: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PersonDetailsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PersonDetailsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load personDetails on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.personDetails).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
