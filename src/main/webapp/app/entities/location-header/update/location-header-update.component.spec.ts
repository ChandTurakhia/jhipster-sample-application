jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LocationHeaderService } from '../service/location-header.service';
import { ILocationHeader, LocationHeader } from '../location-header.model';
import { IAddressHeader } from 'app/entities/address-header/address-header.model';
import { AddressHeaderService } from 'app/entities/address-header/service/address-header.service';

import { LocationHeaderUpdateComponent } from './location-header-update.component';

describe('Component Tests', () => {
  describe('LocationHeader Management Update Component', () => {
    let comp: LocationHeaderUpdateComponent;
    let fixture: ComponentFixture<LocationHeaderUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let locationHeaderService: LocationHeaderService;
    let addressHeaderService: AddressHeaderService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LocationHeaderUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LocationHeaderUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LocationHeaderUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      locationHeaderService = TestBed.inject(LocationHeaderService);
      addressHeaderService = TestBed.inject(AddressHeaderService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call addressHeader query and add missing value', () => {
        const locationHeader: ILocationHeader = { id: 456 };
        const addressHeader: IAddressHeader = { id: 46396 };
        locationHeader.addressHeader = addressHeader;

        const addressHeaderCollection: IAddressHeader[] = [{ id: 4636 }];
        spyOn(addressHeaderService, 'query').and.returnValue(of(new HttpResponse({ body: addressHeaderCollection })));
        const expectedCollection: IAddressHeader[] = [addressHeader, ...addressHeaderCollection];
        spyOn(addressHeaderService, 'addAddressHeaderToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ locationHeader });
        comp.ngOnInit();

        expect(addressHeaderService.query).toHaveBeenCalled();
        expect(addressHeaderService.addAddressHeaderToCollectionIfMissing).toHaveBeenCalledWith(addressHeaderCollection, addressHeader);
        expect(comp.addressHeadersCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const locationHeader: ILocationHeader = { id: 456 };
        const addressHeader: IAddressHeader = { id: 41930 };
        locationHeader.addressHeader = addressHeader;

        activatedRoute.data = of({ locationHeader });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(locationHeader));
        expect(comp.addressHeadersCollection).toContain(addressHeader);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const locationHeader = { id: 123 };
        spyOn(locationHeaderService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ locationHeader });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: locationHeader }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(locationHeaderService.update).toHaveBeenCalledWith(locationHeader);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const locationHeader = new LocationHeader();
        spyOn(locationHeaderService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ locationHeader });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: locationHeader }));
        saveSubject.complete();

        // THEN
        expect(locationHeaderService.create).toHaveBeenCalledWith(locationHeader);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const locationHeader = { id: 123 };
        spyOn(locationHeaderService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ locationHeader });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(locationHeaderService.update).toHaveBeenCalledWith(locationHeader);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackAddressHeaderById', () => {
        it('Should return tracked AddressHeader primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAddressHeaderById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
