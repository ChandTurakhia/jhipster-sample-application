import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AddressHeaderComponent } from '../list/address-header.component';
import { AddressHeaderDetailComponent } from '../detail/address-header-detail.component';
import { AddressHeaderUpdateComponent } from '../update/address-header-update.component';
import { AddressHeaderRoutingResolveService } from './address-header-routing-resolve.service';

const addressHeaderRoute: Routes = [
  {
    path: '',
    component: AddressHeaderComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AddressHeaderDetailComponent,
    resolve: {
      addressHeader: AddressHeaderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AddressHeaderUpdateComponent,
    resolve: {
      addressHeader: AddressHeaderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AddressHeaderUpdateComponent,
    resolve: {
      addressHeader: AddressHeaderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(addressHeaderRoute)],
  exports: [RouterModule],
})
export class AddressHeaderRoutingModule {}
