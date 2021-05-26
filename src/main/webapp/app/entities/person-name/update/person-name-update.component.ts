import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPersonName, PersonName } from '../person-name.model';
import { PersonNameService } from '../service/person-name.service';

@Component({
  selector: 'jhi-person-name-update',
  templateUrl: './person-name-update.component.html',
})
export class PersonNameUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    personId: [],
    firstName: [],
    middleName: [],
    lastName: [],
    secondLastName: [],
    preferredName: [],
    prefixCode: [],
    suffixCode: [],
    validFrom: [],
    validTo: [],
  });

  constructor(protected personNameService: PersonNameService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personName }) => {
      if (personName.id === undefined) {
        const today = dayjs().startOf('day');
        personName.validFrom = today;
        personName.validTo = today;
      }

      this.updateForm(personName);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const personName = this.createFromForm();
    if (personName.id !== undefined) {
      this.subscribeToSaveResponse(this.personNameService.update(personName));
    } else {
      this.subscribeToSaveResponse(this.personNameService.create(personName));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPersonName>>): void {
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

  protected updateForm(personName: IPersonName): void {
    this.editForm.patchValue({
      id: personName.id,
      personId: personName.personId,
      firstName: personName.firstName,
      middleName: personName.middleName,
      lastName: personName.lastName,
      secondLastName: personName.secondLastName,
      preferredName: personName.preferredName,
      prefixCode: personName.prefixCode,
      suffixCode: personName.suffixCode,
      validFrom: personName.validFrom ? personName.validFrom.format(DATE_TIME_FORMAT) : null,
      validTo: personName.validTo ? personName.validTo.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IPersonName {
    return {
      ...new PersonName(),
      id: this.editForm.get(['id'])!.value,
      personId: this.editForm.get(['personId'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      middleName: this.editForm.get(['middleName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      secondLastName: this.editForm.get(['secondLastName'])!.value,
      preferredName: this.editForm.get(['preferredName'])!.value,
      prefixCode: this.editForm.get(['prefixCode'])!.value,
      suffixCode: this.editForm.get(['suffixCode'])!.value,
      validFrom: this.editForm.get(['validFrom'])!.value ? dayjs(this.editForm.get(['validFrom'])!.value, DATE_TIME_FORMAT) : undefined,
      validTo: this.editForm.get(['validTo'])!.value ? dayjs(this.editForm.get(['validTo'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }
}
