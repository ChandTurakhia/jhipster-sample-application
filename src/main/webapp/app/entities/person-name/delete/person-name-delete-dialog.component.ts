import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPersonName } from '../person-name.model';
import { PersonNameService } from '../service/person-name.service';

@Component({
  templateUrl: './person-name-delete-dialog.component.html',
})
export class PersonNameDeleteDialogComponent {
  personName?: IPersonName;

  constructor(protected personNameService: PersonNameService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.personNameService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
