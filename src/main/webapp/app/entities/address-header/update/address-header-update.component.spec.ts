jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AddressHeaderService } from '../service/address-header.service';
import { IAddressHeader, AddressHeader } from '../address-header.model';
import { IPersonAddress } from 'app/entities/person-address/person-address.model';
import { PersonAddressService } from 'app/entities/person-address/service/person-address.service';

import { AddressHeaderUpdateComponent } from './address-header-update.component';

describe('Component Tests', () => {
  describe('AddressHeader Management Update Component', () => {
    let comp: AddressHeaderUpdateComponent;
    let fixture: ComponentFixture<AddressHeaderUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let addressHeaderService: AddressHeaderService;
    let personAddressService: PersonAddressService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AddressHeaderUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AddressHeaderUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AddressHeaderUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      addressHeaderService = TestBed.inject(AddressHeaderService);
      personAddressService = TestBed.inject(PersonAddressService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call personAddress query and add missing value', () => {
        const addressHeader: IAddressHeader = { id: 456 };
        const personAddress: IPersonAddress = { id: 60547 };
        addressHeader.personAddress = personAddress;

        const personAddressCollection: IPersonAddress[] = [{ id: 4048 }];
        spyOn(personAddressService, 'query').and.returnValue(of(new HttpResponse({ body: personAddressCollection })));
        const expectedCollection: IPersonAddress[] = [personAddress, ...personAddressCollection];
        spyOn(personAddressService, 'addPersonAddressToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ addressHeader });
        comp.ngOnInit();

        expect(personAddressService.query).toHaveBeenCalled();
        expect(personAddressService.addPersonAddressToCollectionIfMissing).toHaveBeenCalledWith(personAddressCollection, personAddress);
        expect(comp.personAddressesCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const addressHeader: IAddressHeader = { id: 456 };
        const personAddress: IPersonAddress = { id: 97542 };
        addressHeader.personAddress = personAddress;

        activatedRoute.data = of({ addressHeader });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(addressHeader));
        expect(comp.personAddressesCollection).toContain(personAddress);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const addressHeader = { id: 123 };
        spyOn(addressHeaderService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ addressHeader });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: addressHeader }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(addressHeaderService.update).toHaveBeenCalledWith(addressHeader);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const addressHeader = new AddressHeader();
        spyOn(addressHeaderService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ addressHeader });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: addressHeader }));
        saveSubject.complete();

        // THEN
        expect(addressHeaderService.create).toHaveBeenCalledWith(addressHeader);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const addressHeader = { id: 123 };
        spyOn(addressHeaderService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ addressHeader });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(addressHeaderService.update).toHaveBeenCalledWith(addressHeader);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPersonAddressById', () => {
        it('Should return tracked PersonAddress primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPersonAddressById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
