import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { PersonDetailsComponent } from './list/person-details.component';
import { PersonDetailsDetailComponent } from './detail/person-details-detail.component';
import { PersonDetailsUpdateComponent } from './update/person-details-update.component';
import { PersonDetailsDeleteDialogComponent } from './delete/person-details-delete-dialog.component';
import { PersonDetailsRoutingModule } from './route/person-details-routing.module';

@NgModule({
  imports: [SharedModule, PersonDetailsRoutingModule],
  declarations: [PersonDetailsComponent, PersonDetailsDetailComponent, PersonDetailsUpdateComponent, PersonDetailsDeleteDialogComponent],
  entryComponents: [PersonDetailsDeleteDialogComponent],
})
export class PersonDetailsModule {}
