import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PersonNameComponent } from '../list/person-name.component';
import { PersonNameDetailComponent } from '../detail/person-name-detail.component';
import { PersonNameUpdateComponent } from '../update/person-name-update.component';
import { PersonNameRoutingResolveService } from './person-name-routing-resolve.service';

const personNameRoute: Routes = [
  {
    path: '',
    component: PersonNameComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PersonNameDetailComponent,
    resolve: {
      personName: PersonNameRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PersonNameUpdateComponent,
    resolve: {
      personName: PersonNameRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PersonNameUpdateComponent,
    resolve: {
      personName: PersonNameRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(personNameRoute)],
  exports: [RouterModule],
})
export class PersonNameRoutingModule {}
