import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { PersonAddressComponent } from './list/person-address.component';
import { PersonAddressDetailComponent } from './detail/person-address-detail.component';
import { PersonAddressUpdateComponent } from './update/person-address-update.component';
import { PersonAddressDeleteDialogComponent } from './delete/person-address-delete-dialog.component';
import { PersonAddressRoutingModule } from './route/person-address-routing.module';

@NgModule({
  imports: [SharedModule, PersonAddressRoutingModule],
  declarations: [PersonAddressComponent, PersonAddressDetailComponent, PersonAddressUpdateComponent, PersonAddressDeleteDialogComponent],
  entryComponents: [PersonAddressDeleteDialogComponent],
})
export class PersonAddressModule {}
