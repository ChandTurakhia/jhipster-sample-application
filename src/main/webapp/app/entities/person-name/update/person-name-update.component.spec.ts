jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PersonNameService } from '../service/person-name.service';
import { IPersonName, PersonName } from '../person-name.model';

import { PersonNameUpdateComponent } from './person-name-update.component';

describe('Component Tests', () => {
  describe('PersonName Management Update Component', () => {
    let comp: PersonNameUpdateComponent;
    let fixture: ComponentFixture<PersonNameUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let personNameService: PersonNameService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PersonNameUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PersonNameUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PersonNameUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      personNameService = TestBed.inject(PersonNameService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const personName: IPersonName = { id: 456 };

        activatedRoute.data = of({ personName });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(personName));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const personName = { id: 123 };
        spyOn(personNameService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ personName });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: personName }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(personNameService.update).toHaveBeenCalledWith(personName);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const personName = new PersonName();
        spyOn(personNameService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ personName });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: personName }));
        saveSubject.complete();

        // THEN
        expect(personNameService.create).toHaveBeenCalledWith(personName);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const personName = { id: 123 };
        spyOn(personNameService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ personName });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(personNameService.update).toHaveBeenCalledWith(personName);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
