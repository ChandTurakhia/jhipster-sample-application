jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PersonService } from '../service/person.service';
import { IPerson, Person } from '../person.model';
import { IPersonName } from 'app/entities/person-name/person-name.model';
import { PersonNameService } from 'app/entities/person-name/service/person-name.service';
import { IPersonAddress } from 'app/entities/person-address/person-address.model';
import { PersonAddressService } from 'app/entities/person-address/service/person-address.service';
import { IPersonLanguage } from 'app/entities/person-language/person-language.model';
import { PersonLanguageService } from 'app/entities/person-language/service/person-language.service';

import { PersonUpdateComponent } from './person-update.component';

describe('Component Tests', () => {
  describe('Person Management Update Component', () => {
    let comp: PersonUpdateComponent;
    let fixture: ComponentFixture<PersonUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let personService: PersonService;
    let personNameService: PersonNameService;
    let personAddressService: PersonAddressService;
    let personLanguageService: PersonLanguageService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PersonUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PersonUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PersonUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      personService = TestBed.inject(PersonService);
      personNameService = TestBed.inject(PersonNameService);
      personAddressService = TestBed.inject(PersonAddressService);
      personLanguageService = TestBed.inject(PersonLanguageService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call PersonName query and add missing value', () => {
        const person: IPerson = { id: 456 };
        const personName: IPersonName = { id: 94269 };
        person.personName = personName;

        const personNameCollection: IPersonName[] = [{ id: 38473 }];
        spyOn(personNameService, 'query').and.returnValue(of(new HttpResponse({ body: personNameCollection })));
        const additionalPersonNames = [personName];
        const expectedCollection: IPersonName[] = [...additionalPersonNames, ...personNameCollection];
        spyOn(personNameService, 'addPersonNameToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ person });
        comp.ngOnInit();

        expect(personNameService.query).toHaveBeenCalled();
        expect(personNameService.addPersonNameToCollectionIfMissing).toHaveBeenCalledWith(personNameCollection, ...additionalPersonNames);
        expect(comp.personNamesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call PersonAddress query and add missing value', () => {
        const person: IPerson = { id: 456 };
        const personAddress: IPersonAddress = { id: 49170 };
        person.personAddress = personAddress;

        const personAddressCollection: IPersonAddress[] = [{ id: 57291 }];
        spyOn(personAddressService, 'query').and.returnValue(of(new HttpResponse({ body: personAddressCollection })));
        const additionalPersonAddresses = [personAddress];
        const expectedCollection: IPersonAddress[] = [...additionalPersonAddresses, ...personAddressCollection];
        spyOn(personAddressService, 'addPersonAddressToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ person });
        comp.ngOnInit();

        expect(personAddressService.query).toHaveBeenCalled();
        expect(personAddressService.addPersonAddressToCollectionIfMissing).toHaveBeenCalledWith(
          personAddressCollection,
          ...additionalPersonAddresses
        );
        expect(comp.personAddressesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call PersonLanguage query and add missing value', () => {
        const person: IPerson = { id: 456 };
        const personLanguage: IPersonLanguage = { id: 17185 };
        person.personLanguage = personLanguage;

        const personLanguageCollection: IPersonLanguage[] = [{ id: 87149 }];
        spyOn(personLanguageService, 'query').and.returnValue(of(new HttpResponse({ body: personLanguageCollection })));
        const additionalPersonLanguages = [personLanguage];
        const expectedCollection: IPersonLanguage[] = [...additionalPersonLanguages, ...personLanguageCollection];
        spyOn(personLanguageService, 'addPersonLanguageToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ person });
        comp.ngOnInit();

        expect(personLanguageService.query).toHaveBeenCalled();
        expect(personLanguageService.addPersonLanguageToCollectionIfMissing).toHaveBeenCalledWith(
          personLanguageCollection,
          ...additionalPersonLanguages
        );
        expect(comp.personLanguagesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const person: IPerson = { id: 456 };
        const personName: IPersonName = { id: 52138 };
        person.personName = personName;
        const personAddress: IPersonAddress = { id: 32271 };
        person.personAddress = personAddress;
        const personLanguage: IPersonLanguage = { id: 74332 };
        person.personLanguage = personLanguage;

        activatedRoute.data = of({ person });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(person));
        expect(comp.personNamesSharedCollection).toContain(personName);
        expect(comp.personAddressesSharedCollection).toContain(personAddress);
        expect(comp.personLanguagesSharedCollection).toContain(personLanguage);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const person = { id: 123 };
        spyOn(personService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ person });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: person }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(personService.update).toHaveBeenCalledWith(person);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const person = new Person();
        spyOn(personService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ person });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: person }));
        saveSubject.complete();

        // THEN
        expect(personService.create).toHaveBeenCalledWith(person);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const person = { id: 123 };
        spyOn(personService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ person });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(personService.update).toHaveBeenCalledWith(person);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPersonNameById', () => {
        it('Should return tracked PersonName primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPersonNameById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackPersonAddressById', () => {
        it('Should return tracked PersonAddress primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPersonAddressById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackPersonLanguageById', () => {
        it('Should return tracked PersonLanguage primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPersonLanguageById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
