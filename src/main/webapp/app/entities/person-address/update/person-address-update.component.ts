import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPersonAddress, PersonAddress } from '../person-address.model';
import { PersonAddressService } from '../service/person-address.service';

@Component({
  selector: 'jhi-person-address-update',
  templateUrl: './person-address-update.component.html',
})
export class PersonAddressUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    addressTypeCode: [],
    validFrom: [],
    validTo: [],
  });

  constructor(protected personAddressService: PersonAddressService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personAddress }) => {
      if (personAddress.id === undefined) {
        const today = dayjs().startOf('day');
        personAddress.validFrom = today;
        personAddress.validTo = today;
      }

      this.updateForm(personAddress);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const personAddress = this.createFromForm();
    if (personAddress.id !== undefined) {
      this.subscribeToSaveResponse(this.personAddressService.update(personAddress));
    } else {
      this.subscribeToSaveResponse(this.personAddressService.create(personAddress));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPersonAddress>>): void {
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

  protected updateForm(personAddress: IPersonAddress): void {
    this.editForm.patchValue({
      id: personAddress.id,
      addressTypeCode: personAddress.addressTypeCode,
      validFrom: personAddress.validFrom ? personAddress.validFrom.format(DATE_TIME_FORMAT) : null,
      validTo: personAddress.validTo ? personAddress.validTo.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IPersonAddress {
    return {
      ...new PersonAddress(),
      id: this.editForm.get(['id'])!.value,
      addressTypeCode: this.editForm.get(['addressTypeCode'])!.value,
      validFrom: this.editForm.get(['validFrom'])!.value ? dayjs(this.editForm.get(['validFrom'])!.value, DATE_TIME_FORMAT) : undefined,
      validTo: this.editForm.get(['validTo'])!.value ? dayjs(this.editForm.get(['validTo'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }
}
