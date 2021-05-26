import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPersonLanguage } from '../person-language.model';
import { PersonLanguageService } from '../service/person-language.service';
import { PersonLanguageDeleteDialogComponent } from '../delete/person-language-delete-dialog.component';

@Component({
  selector: 'jhi-person-language',
  templateUrl: './person-language.component.html',
})
export class PersonLanguageComponent implements OnInit {
  personLanguages?: IPersonLanguage[];
  isLoading = false;

  constructor(protected personLanguageService: PersonLanguageService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.personLanguageService.query().subscribe(
      (res: HttpResponse<IPersonLanguage[]>) => {
        this.isLoading = false;
        this.personLanguages = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPersonLanguage): number {
    return item.id!;
  }

  delete(personLanguage: IPersonLanguage): void {
    const modalRef = this.modalService.open(PersonLanguageDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.personLanguage = personLanguage;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
