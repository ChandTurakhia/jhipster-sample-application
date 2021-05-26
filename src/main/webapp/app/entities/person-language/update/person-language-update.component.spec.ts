jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PersonLanguageService } from '../service/person-language.service';
import { IPersonLanguage, PersonLanguage } from '../person-language.model';

import { PersonLanguageUpdateComponent } from './person-language-update.component';

describe('Component Tests', () => {
  describe('PersonLanguage Management Update Component', () => {
    let comp: PersonLanguageUpdateComponent;
    let fixture: ComponentFixture<PersonLanguageUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let personLanguageService: PersonLanguageService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PersonLanguageUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PersonLanguageUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PersonLanguageUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      personLanguageService = TestBed.inject(PersonLanguageService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const personLanguage: IPersonLanguage = { id: 456 };

        activatedRoute.data = of({ personLanguage });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(personLanguage));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const personLanguage = { id: 123 };
        spyOn(personLanguageService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ personLanguage });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: personLanguage }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(personLanguageService.update).toHaveBeenCalledWith(personLanguage);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const personLanguage = new PersonLanguage();
        spyOn(personLanguageService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ personLanguage });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: personLanguage }));
        saveSubject.complete();

        // THEN
        expect(personLanguageService.create).toHaveBeenCalledWith(personLanguage);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const personLanguage = { id: 123 };
        spyOn(personLanguageService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ personLanguage });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(personLanguageService.update).toHaveBeenCalledWith(personLanguage);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
