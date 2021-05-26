jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PersonDetailsService } from '../service/person-details.service';
import { IPersonDetails, PersonDetails } from '../person-details.model';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';

import { PersonDetailsUpdateComponent } from './person-details-update.component';

describe('Component Tests', () => {
  describe('PersonDetails Management Update Component', () => {
    let comp: PersonDetailsUpdateComponent;
    let fixture: ComponentFixture<PersonDetailsUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let personDetailsService: PersonDetailsService;
    let personService: PersonService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PersonDetailsUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PersonDetailsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PersonDetailsUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      personDetailsService = TestBed.inject(PersonDetailsService);
      personService = TestBed.inject(PersonService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call person query and add missing value', () => {
        const personDetails: IPersonDetails = { id: 456 };
        const person: IPerson = { id: 2878 };
        personDetails.person = person;

        const personCollection: IPerson[] = [{ id: 35150 }];
        spyOn(personService, 'query').and.returnValue(of(new HttpResponse({ body: personCollection })));
        const expectedCollection: IPerson[] = [person, ...personCollection];
        spyOn(personService, 'addPersonToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ personDetails });
        comp.ngOnInit();

        expect(personService.query).toHaveBeenCalled();
        expect(personService.addPersonToCollectionIfMissing).toHaveBeenCalledWith(personCollection, person);
        expect(comp.peopleCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const personDetails: IPersonDetails = { id: 456 };
        const person: IPerson = { id: 59319 };
        personDetails.person = person;

        activatedRoute.data = of({ personDetails });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(personDetails));
        expect(comp.peopleCollection).toContain(person);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const personDetails = { id: 123 };
        spyOn(personDetailsService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ personDetails });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: personDetails }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(personDetailsService.update).toHaveBeenCalledWith(personDetails);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const personDetails = new PersonDetails();
        spyOn(personDetailsService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ personDetails });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: personDetails }));
        saveSubject.complete();

        // THEN
        expect(personDetailsService.create).toHaveBeenCalledWith(personDetails);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const personDetails = { id: 123 };
        spyOn(personDetailsService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ personDetails });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(personDetailsService.update).toHaveBeenCalledWith(personDetails);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPersonById', () => {
        it('Should return tracked Person primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPersonById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
