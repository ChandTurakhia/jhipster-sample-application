import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILocationHeader } from '../location-header.model';
import { LocationHeaderService } from '../service/location-header.service';
import { LocationHeaderDeleteDialogComponent } from '../delete/location-header-delete-dialog.component';

@Component({
  selector: 'jhi-location-header',
  templateUrl: './location-header.component.html',
})
export class LocationHeaderComponent implements OnInit {
  locationHeaders?: ILocationHeader[];
  isLoading = false;

  constructor(protected locationHeaderService: LocationHeaderService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.locationHeaderService.query().subscribe(
      (res: HttpResponse<ILocationHeader[]>) => {
        this.isLoading = false;
        this.locationHeaders = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ILocationHeader): number {
    return item.id!;
  }

  delete(locationHeader: ILocationHeader): void {
    const modalRef = this.modalService.open(LocationHeaderDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.locationHeader = locationHeader;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
