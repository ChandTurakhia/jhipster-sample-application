import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PersonLanguageDetailComponent } from './person-language-detail.component';

describe('Component Tests', () => {
  describe('PersonLanguage Management Detail Component', () => {
    let comp: PersonLanguageDetailComponent;
    let fixture: ComponentFixture<PersonLanguageDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PersonLanguageDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ personLanguage: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PersonLanguageDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PersonLanguageDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load personLanguage on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.personLanguage).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
