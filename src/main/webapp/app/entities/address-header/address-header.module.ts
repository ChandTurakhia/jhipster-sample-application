import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { AddressHeaderComponent } from './list/address-header.component';
import { AddressHeaderDetailComponent } from './detail/address-header-detail.component';
import { AddressHeaderUpdateComponent } from './update/address-header-update.component';
import { AddressHeaderDeleteDialogComponent } from './delete/address-header-delete-dialog.component';
import { AddressHeaderRoutingModule } from './route/address-header-routing.module';

@NgModule({
  imports: [SharedModule, AddressHeaderRoutingModule],
  declarations: [AddressHeaderComponent, AddressHeaderDetailComponent, AddressHeaderUpdateComponent, AddressHeaderDeleteDialogComponent],
  entryComponents: [AddressHeaderDeleteDialogComponent],
})
export class AddressHeaderModule {}
