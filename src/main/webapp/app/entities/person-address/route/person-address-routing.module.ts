import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PersonAddressComponent } from '../list/person-address.component';
import { PersonAddressDetailComponent } from '../detail/person-address-detail.component';
import { PersonAddressUpdateComponent } from '../update/person-address-update.component';
import { PersonAddressRoutingResolveService } from './person-address-routing-resolve.service';

const personAddressRoute: Routes = [
  {
    path: '',
    component: PersonAddressComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PersonAddressDetailComponent,
    resolve: {
      personAddress: PersonAddressRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PersonAddressUpdateComponent,
    resolve: {
      personAddress: PersonAddressRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PersonAddressUpdateComponent,
    resolve: {
      personAddress: PersonAddressRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(personAddressRoute)],
  exports: [RouterModule],
})
export class PersonAddressRoutingModule {}
