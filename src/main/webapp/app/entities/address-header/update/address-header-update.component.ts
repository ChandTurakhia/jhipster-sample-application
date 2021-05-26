import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAddressHeader, AddressHeader } from '../address-header.model';
import { AddressHeaderService } from '../service/address-header.service';
import { IPersonAddress } from 'app/entities/person-address/person-address.model';
import { PersonAddressService } from 'app/entities/person-address/service/person-address.service';

@Component({
  selector: 'jhi-address-header-update',
  templateUrl: './address-header-update.component.html',
})
export class AddressHeaderUpdateComponent implements OnInit {
  isSaving = false;

  personAddressesCollection: IPersonAddress[] = [];

  editForm = this.fb.group({
    id: [],
    typeCode: [],
    standardized: [],
    addressLine1: [],
    addressLine2: [],
    addressLine3: [],
    cityName: [],
    countyName: [],
    stateCode: [],
    zipCode: [],
    countryName: [],
    personAddress: [],
  });

  constructor(
    protected addressHeaderService: AddressHeaderService,
    protected personAddressService: PersonAddressService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ addressHeader }) => {
      this.updateForm(addressHeader);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const addressHeader = this.createFromForm();
    if (addressHeader.id !== undefined) {
      this.subscribeToSaveResponse(this.addressHeaderService.update(addressHeader));
    } else {
      this.subscribeToSaveResponse(this.addressHeaderService.create(addressHeader));
    }
  }

  trackPersonAddressById(index: number, item: IPersonAddress): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAddressHeader>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(addressHeader: IAddressHeader): void {
    this.editForm.patchValue({
      id: addressHeader.id,
      typeCode: addressHeader.typeCode,
      standardized: addressHeader.standardized,
      addressLine1: addressHeader.addressLine1,
      addressLine2: addressHeader.addressLine2,
      addressLine3: addressHeader.addressLine3,
      cityName: addressHeader.cityName,
      countyName: addressHeader.countyName,
      stateCode: addressHeader.stateCode,
      zipCode: addressHeader.zipCode,
      countryName: addressHeader.countryName,
      personAddress: addressHeader.personAddress,
    });

    this.personAddressesCollection = this.personAddressService.addPersonAddressToCollectionIfMissing(
      this.personAddressesCollection,
      addressHeader.personAddress
    );
  }

  protected loadRelationshipsOptions(): void {
    this.personAddressService
      .query({ filter: 'addressheader-is-null' })
      .pipe(map((res: HttpResponse<IPersonAddress[]>) => res.body ?? []))
      .pipe(
        map((personAddresses: IPersonAddress[]) =>
          this.personAddressService.addPersonAddressToCollectionIfMissing(personAddresses, this.editForm.get('personAddress')!.value)
        )
      )
      .subscribe((personAddresses: IPersonAddress[]) => (this.personAddressesCollection = personAddresses));
  }

  protected createFromForm(): IAddressHeader {
    return {
      ...new AddressHeader(),
      id: this.editForm.get(['id'])!.value,
      typeCode: this.editForm.get(['typeCode'])!.value,
      standardized: this.editForm.get(['standardized'])!.value,
      addressLine1: this.editForm.get(['addressLine1'])!.value,
      addressLine2: this.editForm.get(['addressLine2'])!.value,
      addressLine3: this.editForm.get(['addressLine3'])!.value,
      cityName: this.editForm.get(['cityName'])!.value,
      countyName: this.editForm.get(['countyName'])!.value,
      stateCode: this.editForm.get(['stateCode'])!.value,
      zipCode: this.editForm.get(['zipCode'])!.value,
      countryName: this.editForm.get(['countryName'])!.value,
      personAddress: this.editForm.get(['personAddress'])!.value,
    };
  }
}
