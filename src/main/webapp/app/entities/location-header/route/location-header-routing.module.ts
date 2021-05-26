import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LocationHeaderComponent } from '../list/location-header.component';
import { LocationHeaderDetailComponent } from '../detail/location-header-detail.component';
import { LocationHeaderUpdateComponent } from '../update/location-header-update.component';
import { LocationHeaderRoutingResolveService } from './location-header-routing-resolve.service';

const locationHeaderRoute: Routes = [
  {
    path: '',
    component: LocationHeaderComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LocationHeaderDetailComponent,
    resolve: {
      locationHeader: LocationHeaderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LocationHeaderUpdateComponent,
    resolve: {
      locationHeader: LocationHeaderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LocationHeaderUpdateComponent,
    resolve: {
      locationHeader: LocationHeaderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(locationHeaderRoute)],
  exports: [RouterModule],
})
export class LocationHeaderRoutingModule {}
