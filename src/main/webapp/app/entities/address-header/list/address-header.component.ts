import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAddressHeader } from '../address-header.model';
import { AddressHeaderService } from '../service/address-header.service';
import { AddressHeaderDeleteDialogComponent } from '../delete/address-header-delete-dialog.component';

@Component({
  selector: 'jhi-address-header',
  templateUrl: './address-header.component.html',
})
export class AddressHeaderComponent implements OnInit {
  addressHeaders?: IAddressHeader[];
  isLoading = false;

  constructor(protected addressHeaderService: AddressHeaderService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.addressHeaderService.query().subscribe(
      (res: HttpResponse<IAddressHeader[]>) => {
        this.isLoading = false;
        this.addressHeaders = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IAddressHeader): number {
    return item.id!;
  }

  delete(addressHeader: IAddressHeader): void {
    const modalRef = this.modalService.open(AddressHeaderDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.addressHeader = addressHeader;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
