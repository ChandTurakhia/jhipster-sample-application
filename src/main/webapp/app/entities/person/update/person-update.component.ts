import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPerson, Person } from '../person.model';
import { PersonService } from '../service/person.service';
import { IPersonName } from 'app/entities/person-name/person-name.model';
import { PersonNameService } from 'app/entities/person-name/service/person-name.service';
import { IPersonAddress } from 'app/entities/person-address/person-address.model';
import { PersonAddressService } from 'app/entities/person-address/service/person-address.service';
import { IPersonLanguage } from 'app/entities/person-language/person-language.model';
import { PersonLanguageService } from 'app/entities/person-language/service/person-language.service';

@Component({
  selector: 'jhi-person-update',
  templateUrl: './person-update.component.html',
})
export class PersonUpdateComponent implements OnInit {
  isSaving = false;

  personNamesSharedCollection: IPersonName[] = [];
  personAddressesSharedCollection: IPersonAddress[] = [];
  personLanguagesSharedCollection: IPersonLanguage[] = [];

  editForm = this.fb.group({
    id: [],
    version: [],
    stateCode: [],
    dateOfBirth: [],
    dateOfDeath: [],
    genderTypeCode: [],
    personName: [],
    personAddress: [],
    personLanguage: [],
  });

  constructor(
    protected personService: PersonService,
    protected personNameService: PersonNameService,
    protected personAddressService: PersonAddressService,
    protected personLanguageService: PersonLanguageService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ person }) => {
      if (person.id === undefined) {
        const today = dayjs().startOf('day');
        person.dateOfBirth = today;
        person.dateOfDeath = today;
      }

      this.updateForm(person);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const person = this.createFromForm();
    if (person.id !== undefined) {
      this.subscribeToSaveResponse(this.personService.update(person));
    } else {
      this.subscribeToSaveResponse(this.personService.create(person));
    }
  }

  trackPersonNameById(index: number, item: IPersonName): number {
    return item.id!;
  }

  trackPersonAddressById(index: number, item: IPersonAddress): number {
    return item.id!;
  }

  trackPersonLanguageById(index: number, item: IPersonLanguage): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPerson>>): void {
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

  protected updateForm(person: IPerson): void {
    this.editForm.patchValue({
      id: person.id,
      version: person.version,
      stateCode: person.stateCode,
      dateOfBirth: person.dateOfBirth ? person.dateOfBirth.format(DATE_TIME_FORMAT) : null,
      dateOfDeath: person.dateOfDeath ? person.dateOfDeath.format(DATE_TIME_FORMAT) : null,
      genderTypeCode: person.genderTypeCode,
      personName: person.personName,
      personAddress: person.personAddress,
      personLanguage: person.personLanguage,
    });

    this.personNamesSharedCollection = this.personNameService.addPersonNameToCollectionIfMissing(
      this.personNamesSharedCollection,
      person.personName
    );
    this.personAddressesSharedCollection = this.personAddressService.addPersonAddressToCollectionIfMissing(
      this.personAddressesSharedCollection,
      person.personAddress
    );
    this.personLanguagesSharedCollection = this.personLanguageService.addPersonLanguageToCollectionIfMissing(
      this.personLanguagesSharedCollection,
      person.personLanguage
    );
  }

  protected loadRelationshipsOptions(): void {
    this.personNameService
      .query()
      .pipe(map((res: HttpResponse<IPersonName[]>) => res.body ?? []))
      .pipe(
        map((personNames: IPersonName[]) =>
          this.personNameService.addPersonNameToCollectionIfMissing(personNames, this.editForm.get('personName')!.value)
        )
      )
      .subscribe((personNames: IPersonName[]) => (this.personNamesSharedCollection = personNames));

    this.personAddressService
      .query()
      .pipe(map((res: HttpResponse<IPersonAddress[]>) => res.body ?? []))
      .pipe(
        map((personAddresses: IPersonAddress[]) =>
          this.personAddressService.addPersonAddressToCollectionIfMissing(personAddresses, this.editForm.get('personAddress')!.value)
        )
      )
      .subscribe((personAddresses: IPersonAddress[]) => (this.personAddressesSharedCollection = personAddresses));

    this.personLanguageService
      .query()
      .pipe(map((res: HttpResponse<IPersonLanguage[]>) => res.body ?? []))
      .pipe(
        map((personLanguages: IPersonLanguage[]) =>
          this.personLanguageService.addPersonLanguageToCollectionIfMissing(personLanguages, this.editForm.get('personLanguage')!.value)
        )
      )
      .subscribe((personLanguages: IPersonLanguage[]) => (this.personLanguagesSharedCollection = personLanguages));
  }

  protected createFromForm(): IPerson {
    return {
      ...new Person(),
      id: this.editForm.get(['id'])!.value,
      version: this.editForm.get(['version'])!.value,
      stateCode: this.editForm.get(['stateCode'])!.value,
      dateOfBirth: this.editForm.get(['dateOfBirth'])!.value
        ? dayjs(this.editForm.get(['dateOfBirth'])!.value, DATE_TIME_FORMAT)
        : undefined,
      dateOfDeath: this.editForm.get(['dateOfDeath'])!.value
        ? dayjs(this.editForm.get(['dateOfDeath'])!.value, DATE_TIME_FORMAT)
        : undefined,
      genderTypeCode: this.editForm.get(['genderTypeCode'])!.value,
      personName: this.editForm.get(['personName'])!.value,
      personAddress: this.editForm.get(['personAddress'])!.value,
      personLanguage: this.editForm.get(['personLanguage'])!.value,
    };
  }
}
