import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PersonLanguageComponent } from '../list/person-language.component';
import { PersonLanguageDetailComponent } from '../detail/person-language-detail.component';
import { PersonLanguageUpdateComponent } from '../update/person-language-update.component';
import { PersonLanguageRoutingResolveService } from './person-language-routing-resolve.service';

const personLanguageRoute: Routes = [
  {
    path: '',
    component: PersonLanguageComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PersonLanguageDetailComponent,
    resolve: {
      personLanguage: PersonLanguageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PersonLanguageUpdateComponent,
    resolve: {
      personLanguage: PersonLanguageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PersonLanguageUpdateComponent,
    resolve: {
      personLanguage: PersonLanguageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(personLanguageRoute)],
  exports: [RouterModule],
})
export class PersonLanguageRoutingModule {}
