import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { LocationHeaderComponent } from './list/location-header.component';
import { LocationHeaderDetailComponent } from './detail/location-header-detail.component';
import { LocationHeaderUpdateComponent } from './update/location-header-update.component';
import { LocationHeaderDeleteDialogComponent } from './delete/location-header-delete-dialog.component';
import { LocationHeaderRoutingModule } from './route/location-header-routing.module';

@NgModule({
  imports: [SharedModule, LocationHeaderRoutingModule],
  declarations: [
    LocationHeaderComponent,
    LocationHeaderDetailComponent,
    LocationHeaderUpdateComponent,
    LocationHeaderDeleteDialogComponent,
  ],
  entryComponents: [LocationHeaderDeleteDialogComponent],
})
export class LocationHeaderModule {}
