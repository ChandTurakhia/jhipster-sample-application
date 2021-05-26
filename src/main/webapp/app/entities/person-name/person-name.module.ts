import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { PersonNameComponent } from './list/person-name.component';
import { PersonNameDetailComponent } from './detail/person-name-detail.component';
import { PersonNameUpdateComponent } from './update/person-name-update.component';
import { PersonNameDeleteDialogComponent } from './delete/person-name-delete-dialog.component';
import { PersonNameRoutingModule } from './route/person-name-routing.module';

@NgModule({
  imports: [SharedModule, PersonNameRoutingModule],
  declarations: [PersonNameComponent, PersonNameDetailComponent, PersonNameUpdateComponent, PersonNameDeleteDialogComponent],
  entryComponents: [PersonNameDeleteDialogComponent],
})
export class PersonNameModule {}
