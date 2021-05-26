import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPersonLanguage } from '../person-language.model';
import { PersonLanguageService } from '../service/person-language.service';

@Component({
  templateUrl: './person-language-delete-dialog.component.html',
})
export class PersonLanguageDeleteDialogComponent {
  personLanguage?: IPersonLanguage;

  constructor(protected personLanguageService: PersonLanguageService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.personLanguageService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
