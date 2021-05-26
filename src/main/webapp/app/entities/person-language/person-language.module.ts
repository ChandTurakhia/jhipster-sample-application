import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { PersonLanguageComponent } from './list/person-language.component';
import { PersonLanguageDetailComponent } from './detail/person-language-detail.component';
import { PersonLanguageUpdateComponent } from './update/person-language-update.component';
import { PersonLanguageDeleteDialogComponent } from './delete/person-language-delete-dialog.component';
import { PersonLanguageRoutingModule } from './route/person-language-routing.module';

@NgModule({
  imports: [SharedModule, PersonLanguageRoutingModule],
  declarations: [
    PersonLanguageComponent,
    PersonLanguageDetailComponent,
    PersonLanguageUpdateComponent,
    PersonLanguageDeleteDialogComponent,
  ],
  entryComponents: [PersonLanguageDeleteDialogComponent],
})
export class PersonLanguageModule {}
