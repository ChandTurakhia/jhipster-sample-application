import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PersonDetailsComponent } from '../list/person-details.component';
import { PersonDetailsDetailComponent } from '../detail/person-details-detail.component';
import { PersonDetailsUpdateComponent } from '../update/person-details-update.component';
import { PersonDetailsRoutingResolveService } from './person-details-routing-resolve.service';

const personDetailsRoute: Routes = [
  {
    path: '',
    component: PersonDetailsComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PersonDetailsDetailComponent,
    resolve: {
      personDetails: PersonDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PersonDetailsUpdateComponent,
    resolve: {
      personDetails: PersonDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PersonDetailsUpdateComponent,
    resolve: {
      personDetails: PersonDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(personDetailsRoute)],
  exports: [RouterModule],
})
export class PersonDetailsRoutingModule {}
