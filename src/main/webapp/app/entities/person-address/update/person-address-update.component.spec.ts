jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PersonAddressService } from '../service/person-address.service';
import { IPersonAddress, PersonAddress } from '../person-address.model';

import { PersonAddressUpdateComponent } from './person-address-update.component';

describe('Component Tests', () => {
  describe('PersonAddress Management Update Component', () => {
    let comp: PersonAddressUpdateComponent;
    let fixture: ComponentFixture<PersonAddressUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let personAddressService: PersonAddressService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PersonAddressUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PersonAddressUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PersonAddressUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      personAddressService = TestBed.inject(PersonAddressService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const personAddress: IPersonAddress = { id: 456 };

        activatedRoute.data = of({ personAddress });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(personAddress));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const personAddress = { id: 123 };
        spyOn(personAddressService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ personAddress });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: personAddress }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(personAddressService.update).toHaveBeenCalledWith(personAddress);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const personAddress = new PersonAddress();
        spyOn(personAddressService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ personAddress });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: personAddress }));
        saveSubject.complete();

        // THEN
        expect(personAddressService.create).toHaveBeenCalledWith(personAddress);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const personAddress = { id: 123 };
        spyOn(personAddressService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ personAddress });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(personAddressService.update).toHaveBeenCalledWith(personAddress);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
