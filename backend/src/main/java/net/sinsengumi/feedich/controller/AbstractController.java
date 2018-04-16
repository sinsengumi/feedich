package net.sinsengumi.feedich.controller;

import net.sinsengumi.feedich.exception.NotFoundException;
import net.sinsengumi.feedich.exception.UnauthorizedException;
import net.sinsengumi.feedich.model.Authorizable;

public abstract class AbstractController {

    protected String redirect(String to) {
        return "redirect:" + to;
    }

    protected String forward(String to) {
        return "forward:" + to;
    }

    protected void authorizeResource(Authorizable authorizable, Integer owner) {
        if (authorizable == null) {
            throw new NotFoundException(String.format("resource is not found. owner = %d", owner));
        }
        if (owner == null) {
            throw new NotFoundException("owner is not found.");
        }

        if (authorizable.getOwner() != owner) {
            throw new UnauthorizedException(String.format(
                    "resource is not permitted. resourceOwner = %s, accessUser = %s", authorizable.getOwner(), owner));
        }
    }
}
