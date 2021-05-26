import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAddressHeader } from '../address-header.model';
import { AddressHeaderService } from '../service/address-header.service';

@Component({
  templateUrl: './address-header-delete-dialog.component.html',
})
export class AddressHeaderDeleteDialogComponent {
  addressHeader?: IAddressHeader;

  constructor(protected addressHeaderService: AddressHeaderService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.addressHeaderService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
